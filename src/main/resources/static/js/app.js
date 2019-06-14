new Vue({
  el: '#rtc-rapper',
  data: {
    webSocket: null,
    urlValue: 'ws://192.168.20.2:8082/websocket',
    urlRtsp: 'rtsp://admin:qwer1234@192.168.30.205:554/h264/ch1/sub/av_stream',
    webRtcPeer: null,
    args: null
  },
  mounted() {
    this.init();
  },
  methods: {
    createWebSocket(urlValue) {
      if ('WebSocket' in window) {
        return new WebSocket(urlValue);
      }
      throw new Error('不支持websocket')
    },
    percentNum(num, num2) {
      return (Math.round(num / num2 * 10000) / 100.00 + '%'); // 小数点后两位百分比
    },
    creatSocket() {
      this.webSocket = this.createWebSocket(this.urlValue);
      // 服务器返回数据时执行
      this.webSocket.onmessage = msg => {
        var personJsonList = JSON.parse(msg.data);
        /* 获取横纵坐标*/
        var div = document.getElementById('canvasDiv');
        var tagImg = document.getElementById('tagImg');
        for (var i = 0; i < personJsonList.length; i++) {
          // 设置div的属性
          div.style.width = personJsonList[0]['person']['faceInfo']['rect']['right'] - personJsonList[0]['person']['faceInfo']['rect']['left'] + 'px';
          div.style.height = personJsonList[0]['person']['faceInfo']['rect']['bottom'] - personJsonList[0]['person']['faceInfo']['rect']['top'] + 'px';
          div.style.border = '1px solid blue';// 设置css样式,s设置背景颜色
          div.style.position = 'absolute';
          div.style.top = personJsonList[0]['person']['faceInfo']['rect']['top'] + 'px';
          div.style.left = this.percentNum(personJsonList[0]['person']['faceInfo']['rect']['left'], 640);
          div.style.display = 'block';
          var num = Math.floor(Math.random() * 20);
          tagImg.src = 'lableImg/maleLable/' + num + '.png';
          tagImg.style.position = 'absolute';
          tagImg.style.top = personJsonList[0]['person']['faceInfo']['rect']['top'] - 48 + 'px';
          tagImg.style.left = div.style.left;
          tagImg.style.display = 'block';
          console.log('lableImg/maleLable/' + num + '.png');
          div.innerHTML = '年龄:' + personJsonList[0]['baseLibrary']['age'] + '<br/>' + '姓名:' + personJsonList[0]['baseLibrary']['name'];
        }
      };
    },
    getOpts(args, opts) {
      var result = opts.default || {};
      args.replace(
        new RegExp('([^?=&]+)(=([^&]*))?', 'g'),
        function($0, $1, $2, $3) {
          result[$1] = $3;
        });

      return result;
    },
    showSpinner() {
      for (var i = 0; i < arguments.length; i++) {
        arguments[i].poster = 'img/transparent-1px.png';
        arguments[i].style.background = "center transparent url('img/spinner.gif') no-repeat";
      }
    },
    start() {
      if (!this.urlRtsp) {
        alert('You must set the video source URL first');
        return;
      }
      // 打开socket
      this.creatSocket();
      this.showSpinner(this.videoOutput);
      var options = {
        remoteVideo: this.videoOutput
      };
      const that = this;
      this.webRtcPeer = kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
        function(error) {
          if (error) {
            return console.error(error);
          }
          that.webRtcPeer.generateOffer(that.onOffer);
          that.webRtcPeer.peerConnection.addEventListener('iceconnectionstatechange', function(event) {
            if (that.webRtcPeer && that.webRtcPeer.peerConnection) {
              console.log('oniceconnectionstatechange -> ' + that.webRtcPeer.peerConnection.iceConnectionState);
              console.log('icegatheringstate -> ' + that.webRtcPeer.peerConnection.iceGatheringState);
            }
          });
        });
		setInterval("canvasDivNone()",1000);
    },
    onError(error) {
      if (error) {
        console.error(error);
        stop();
      }
    },
    onOffer(error, sdpOffer) {
      if (error) return this.onError(error);

      const that = this;
      kurentoClient(this.args.ws_uri, function(error, kurentoClient) {
        if (error) return that.onError(error);

        kurentoClient.create('MediaPipeline', function(error, p) {
          if (error) return that.onError(error);

          that.pipeline = p;

          that.pipeline.create('PlayerEndpoint', { networkCache: 0, uri: that.urlRtsp }, function(error, player) {
            if (error) return that.onError(error);

            that.pipeline.create('WebRtcEndpoint', function(error, webRtcEndpoint) {
              if (error) return that.onError(error);

              that.setIceCandidateCallbacks(webRtcEndpoint, that.webRtcPeer, that.onError);

              webRtcEndpoint.processOffer(sdpOffer, function(error, sdpAnswer) {
                if (error) return that.onError(error);

                webRtcEndpoint.gatherCandidates(that.onError);

                that.webRtcPeer.processAnswer(sdpAnswer);
              });

              player.connect(webRtcEndpoint, function(error) {
                if (error) return that.onError(error);

                console.log('PlayerEndpoint-->WebRtcEndpoint connection established');

                player.play(function(error) {
                  if (error) return that.onError(error);

                  console.log('Player playing ...');
                });
              });
            });
          });
        });
      });
    },
    onLoad() {
      this.videoOutput = document.getElementById('videoOutput');

      // const startButton = document.getElementById('start');
      // startButton.addEventListener('click', start);
      // this.start();

      // const stopButton = document.getElementById('stop');
      // stopButton.addEventListener('click', stop);
    },
    stop() {
      if (this.webRtcPeer) {
        this.webRtcPeer.dispose();
        this.webRtcPeer = null;
      }
      if (this.pipeline) {
        this.pipeline.release();
        this.pipeline = null;
      }
      this.hideSpinner(this.videoOutput);
    },
    hideSpinner() {
      for (var i = 0; i < arguments.length; i++) {
        arguments[i].src = '';
        arguments[i].poster = 'img/webrtc.png';
        arguments[i].style.background = '';
      }
    },
    setIceCandidateCallbacks(webRtcEndpoint, webRtcPeer, onError) {
      webRtcPeer.on('icecandidate', function(candidate) {
        console.log('Local icecandidate ' + JSON.stringify(candidate));

        candidate = kurentoClient.register.complexTypes.IceCandidate(candidate);

        webRtcEndpoint.addIceCandidate(candidate, onError);
      });
      webRtcEndpoint.on('OnIceCandidate', function(event) {
        var candidate = event.candidate;

        console.log('Remote icecandidate ' + JSON.stringify(candidate));

        webRtcPeer.addIceCandidate(candidate, onError);
      });
    },
    init() {
      this.args = this.getOpts(location.search,
        {
          default:
            {
              // ws_uri: 'ws://' + location.hostname + ':8888/kurento',
              ws_uri: 'ws://192.168.30.16:8888/kurento',
              ice_servers: undefined
            }
        });

      if (this.args.ice_servers) {
        console.log('Use ICE servers: ' + this.args.ice_servers);
        kurentoUtils.WebRtcPeer.prototype.server.iceServers = JSON.parse(this.args.ice_servers);
      } else {
        console.log('Use freeice');
      }

      window.addEventListener('load', this.onLoad);
	  $(document).delegate('*[data-toggle="lightbox"]', 'click', function (event) {
        event.preventDefault();
        $(this).ekkoLightbox();
      });
    }
  }
})
