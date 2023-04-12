<template>
    <h2>H5与Android通讯</h2>
    <div>
        <button type="button" @click="sendMsgToAndroid">向Android发消息</button>
    </div>
</template>

<script>
export default {
    name: "Bridge",
    mounted() {
        window.receiveAndroidCallback = function receiveAndroidCallback(data) {
            console.log("收到Android发送的消息：" + data);
            return "H5回调内容";
        };
    },
    methods: {
        sendMsgToAndroid() {
            this.sendAndroidCallback("I`m ready", events => {
                console.log("收到Android回调的消息：" + events.data);
            });
        },
        sendAndroidCallback(data, callback) {
            window.android.onmessage = callback;
            window.android.postMessage(data)
        }
    }
}
</script>

<style scoped>

</style>