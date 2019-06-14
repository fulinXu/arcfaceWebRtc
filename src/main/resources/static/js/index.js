/*
* (C) Copyright 2014 Kurento (http://kurento.org/)
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Lesser General Public License
* (LGPL) version 2.1 which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/lgpl-2.1.html
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
*/

var webSocket = null;
var urlValue = "ws://192.168.20.2:8082/websocket";

/**
 * 创建 WebSocKet 的方法
 */
function createWebSocket(urlValue) {
  if ("WebSocket" in window) {
    return new WebSocket(urlValue);
  }
  if ("MozWebSocket" in window) {
    return new MozWebSocket(urlValue);
  }
  console.error("浏览器不支持 WebSocKet");
}

/**
 * 两数就算百分比
 * @param num
 * @param num2
 * @returns {string}
 */

function percentNum(num, num2) {
  return (Math.round(num / num2 * 10000) / 100.00 + "%"); //小数点后两位百分比
}

function canvasDivNone() {
  var tagImg = document.getElementById('tagImg');
  var div = document.getElementById('cannvasDiv');
  tagImg.style.display = "none";
  div.style.display = "none";
}

/**
 * 1, 创建WebSocket
 * 2, WebScoket 的地址为ws协议
 */

function creatSocket() {
  webSocket = createWebSocket(urlValue);
  // 服务器返回数据时执行
  webSocket.onmessage = function (msg) {
    var personJsonList = JSON.parse(msg.data);
    /*获取横纵坐标*/
    var div = document.getElementById('cannvasDiv');
    // var markImg = document.getElementById("markImg");
    var tagImg = document.getElementById("tagImg");
    for (var i = 0; i < personJsonList.length; i++) {
      //设置div的属性
      div.style.width = personJsonList[0]["person"]["faceInfo"]["rect"]["right"] - personJsonList[0]["person"]["faceInfo"]["rect"]["left"] + 'px'
      div.style.height = personJsonList[0]["person"]["faceInfo"]["rect"]["bottom"] - personJsonList[0]["person"]["faceInfo"]["rect"]["top"] + 'px';
      div.style.border = "1px solid blue";//设置css样式,s设置背景颜色
      div.style.position = "absolute";
      // div.style.top = percentNum(personJsonList[0]["person"]["faceInfo"]["rect"]["top"],480);
      console.log(personJsonList[0]["person"]["faceInfo"]["rect"]["top"]);
      div.style.top = personJsonList[0]["person"]["faceInfo"]["rect"]["top"] + 'px';
      div.style.left = percentNum(personJsonList[0]["person"]["faceInfo"]["rect"]["left"], 640);
      div.style.display = "block";
      // markImg.style.top = div.style.top;
      // markImg.style.left = div.style.left;
      // markImg.style.display = "block";
      var num = Math.floor(Math.random() * 20);
      console.log(personJsonList[0]["person"]["faceInfo"]["rect"]["top"]);
      tagImg.src = "lableImg/maleLable/" + num + ".png";
      tagImg.style.position = "absolute";
      tagImg.style.top = personJsonList[0]["person"]["faceInfo"]["rect"]["top"] - 48 + 'px';
      tagImg.style.left = div.style.left;
      tagImg.style.display = "block";
      console.log("lableImg/maleLable/" + num + ".png");
      // }
      // if($('personName').text(personJsonList[0]["baseLibrary"]["gender"])==1){
      //     // $('tagImg').attr("src", "lableImg/femaleLable/"+num+".png");
      //     tagImg.src = "lableImg/maleLable/"+num+".png";
      // }
      div.innerHTML = "年龄:" + personJsonList[0]["baseLibrary"]["age"] + "<br/>" + "姓名:" + personJsonList[0]["baseLibrary"]["name"];
    }

  }
}

function getopts(args, opts) {
  var result = opts.default || {};
  args.replace(
    new RegExp("([^?=&]+)(=([^&]*))?", "g"),
    function ($0, $1, $2, $3) {
      result[$1] = $3;
    });

  return result;
}

var args = getopts(location.search,
  {
    default:
      {
        //ws_uri: 'ws://' + location.hostname + ':8888/kurento',
        ws_uri: 'ws://192.168.30.16:8888/kurento',
        ice_servers: undefined
      }
  });

if (args.ice_servers) {
  console.log("Use ICE servers: " + args.ice_servers);
  kurentoUtils.WebRtcPeer.prototype.server.iceServers = JSON.parse(args.ice_servers);
} else {
  console.log("Use freeice")
}


window.addEventListener('load', function () {
  var videoOutput = document.getElementById('videoOutput');
  var address = document.getElementById('address');
  address.value = 'rtsp://admin:qwer1234@192.168.30.205:554/h264/ch1/sub/av_stream';
  var pipeline;
  var webRtcPeer;

  startButton = document.getElementById('start');
  startButton.addEventListener('click', start);

  stopButton = document.getElementById('stop');
  stopButton.addEventListener('click', stop);


  /*点击启动按钮*/
  function start() {
    if (!address.value) {
      window.alert("You must set the video source URL first");
      return;
    }
    //打开socket
    creatSocket();
    address.disabled = true;
    showSpinner(videoOutput);
    var options = {
      remoteVideo: videoOutput
    };
    webRtcPeer = kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
      function (error) {
        if (error) {
          return console.error(error);
        }
        webRtcPeer.generateOffer(onOffer);
        webRtcPeer.peerConnection.addEventListener('iceconnectionstatechange', function (event) {
          if (webRtcPeer && webRtcPeer.peerConnection) {
            console.log("oniceconnectionstatechange -> " + webRtcPeer.peerConnection.iceConnectionState);
            console.log('icegatheringstate -> ' + webRtcPeer.peerConnection.iceGatheringState);
          }
        });
      });
    setInterval("canvasDivNone()", 1000);
  }


  function onOffer(error, sdpOffer) {
    if (error) return onError(error);

    kurentoClient(args.ws_uri, function (error, kurentoClient) {
      if (error) return onError(error);

      kurentoClient.create("MediaPipeline", function (error, p) {
        if (error) return onError(error);

        pipeline = p;

        pipeline.create("PlayerEndpoint", { networkCache: 0, uri: address.value }, function (error, player) {
          if (error) return onError(error);

          pipeline.create("WebRtcEndpoint", function (error, webRtcEndpoint) {
            if (error) return onError(error);

            setIceCandidateCallbacks(webRtcEndpoint, webRtcPeer, onError);

            webRtcEndpoint.processOffer(sdpOffer, function (error, sdpAnswer) {
              if (error) return onError(error);

              webRtcEndpoint.gatherCandidates(onError);

              webRtcPeer.processAnswer(sdpAnswer);
            });

            player.connect(webRtcEndpoint, function (error) {
              if (error) return onError(error);

              console.log("PlayerEndpoint-->WebRtcEndpoint connection established");

              player.play(function (error) {
                if (error) return onError(error);

                console.log("Player playing ...");
              });
            });
          });
        });
      });
    });
  }

  /*点击停止按钮*/
  function stop() {
    address.disabled = false;
    if (webRtcPeer) {
      webRtcPeer.dispose();
      webRtcPeer = null;
    }
    if (pipeline) {
      pipeline.release();
      pipeline = null;
    }
    hideSpinner(videoOutput);
  }

});

function setIceCandidateCallbacks(webRtcEndpoint, webRtcPeer, onError) {
  webRtcPeer.on('icecandidate', function (candidate) {
    console.log("Local icecandidate " + JSON.stringify(candidate));

    candidate = kurentoClient.register.complexTypes.IceCandidate(candidate);

    webRtcEndpoint.addIceCandidate(candidate, onError);

  });
  webRtcEndpoint.on('OnIceCandidate', function (event) {
    var candidate = event.candidate;

    console.log("Remote icecandidate " + JSON.stringify(candidate));

    webRtcPeer.addIceCandidate(candidate, onError);
  });
}

function onError(error) {
  if (error) {
    console.error(error);
    stop();
  }
}

function showSpinner() {
  for (var i = 0; i < arguments.length; i++) {
    arguments[i].poster = 'img/transparent-1px.png';
    arguments[i].style.background = "center transparent url('img/spinner.gif') no-repeat";
  }
}

function hideSpinner() {
  for (var i = 0; i < arguments.length; i++) {
    arguments[i].src = '';
    arguments[i].poster = 'img/webrtc.png';
    arguments[i].style.background = '';
  }
}

/**
 * Lightbox utility (to display media pipeline image in a modal dialog)
 */
$(document).delegate('*[data-toggle="lightbox"]', 'click', function (event) {
  event.preventDefault();
  $(this).ekkoLightbox();
});

