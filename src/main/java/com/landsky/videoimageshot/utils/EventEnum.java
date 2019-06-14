package com.landsky.videoimageshot.utils;



/***
 ** @category 事件枚举...
 ** @author qing.yunhui
 ** @email: qingyunhui@landsky.cn
 ** @createTime: 2019/2/13-19:32
 **/
public class EventEnum {

    /**
     * <p>事件状态</p>
     * 0：瞬时事件，保存；1：事件开始，保存；2：事件结束，更新结束时间；3：事件脉冲，客户端和服务器使用，CMS不用；4：事件更新，联动结果更新
     * */
    public enum EventStatus implements ICommonEnum {
        TRANSIENT_EVENT_SAVE("0","瞬时事件，保存"),
        EVENT_BEGINS_SAVE("1","事件开始，保存"),
        END_EVENT_UPDATE_TIME("2","事件结束，更新结束时间"),
        EVENT_PULSE_CLIENT_AND_SERVER_USAGE("3","事件脉冲，客户端和服务器使用,CMS不用"),
        EVENT_UPDATE_LINKAGE_RESULT_UPDATE("4","事件更新，联动结果更新");
        private String key;
        private String value;
        EventStatus(String key,String value){
            this.key=key;
            this.value=value;
        }
        @Override
        public Integer getCode() {
            return Integer.parseInt(key);
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

    }

    /**
     * <p>联动类型: 参见联动类型定义：4 为图片联动，2 是录像联动 </p>
     * 当trigger_type为 2 时，该字段为录像结果格式：监控点名称,录像计划Id,录像类型,监控点编号;
     * trigger_type为 4 时，该字段为图片联动结果，格式为监控点名称,联动url,格式：cam1,url1;cam2,url2;
     * 客户端确认信息:user_name;confirm_desc;user_ip;）
     * */
    public enum TriggerType implements ICommonEnum{
        VIDEO_LINKAGE("2","录像联动"),
        PICTURE_LINKAGE("4","图片联动");
        private String key;
        private String value;
        TriggerType(String key,String value){
            this.key=key;
            this.value=value;
        }
        @Override
        public Integer getCode() {
            return Integer.parseInt(key);
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    /**
     * <p>事件类型-后续业务，请自行添加</p>
     * */
    public enum EventType implements ICommonEnum{
        DEVICE_DROPPED("66049","设备掉线"),
        DEVICE_DROPPED_RECOVERY("66109","设备掉线恢复"),
        EMERGENCY_HELP_ALARM("327687","紧急求助报警"),
        FACE_SNAP("131668","人脸比对报警"),
        STRANGER_ALARM("131670","陌生人报警");
        private String key;
        private String value;
        EventType(String key,String value){
            this.key=key;
            this.value=value;
        }
        @Override
        public Integer getCode() {
            return Integer.parseInt(key);
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        /**
         * <p>判断是否是设备掉线事件类型</p>
         * */
        public static boolean isDeviceDropped(int eventType){
            return DEVICE_DROPPED.getCode().compareTo(eventType)==0;
        }
        /**
         * <p>判断是否是设备掉线恢复事件类型</p>
         * */
        public static boolean isDeviceDroppedRecovery(int eventType){
            return DEVICE_DROPPED_RECOVERY.getCode().compareTo(eventType)==0;
        }

        /**
         * <p>判断是事件类型，是否是，掉线或掉线恢复类型的事件</p>
         * */
        public static boolean isOnlineAndUnOnline(int eventType){
            return (isDeviceDroppedRecovery(eventType) || isDeviceDropped(eventType));
        }

        /**
         * <p>判断是否是紧急求助报警事件类型</p>
         * */
        public static boolean isEmergencyHelpAlarm(int eventType){
            return EMERGENCY_HELP_ALARM.getCode().compareTo(eventType)==0;
        }

    }

}
