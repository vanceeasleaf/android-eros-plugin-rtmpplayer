# Android Eros plugin RtmpPlayer

** eros weex 视频流播放插件，从(https://github.com/Truiton/RTMPPlayer)迁移过来 **

为了实现视频通话，发送方要录像并推送给服务器，服务采用直播流 rtmp 的形式推送给客户端。前者叫推流，后者叫播放器。这个就是个用 vlc 实现的播放器。

vlc 只支持 armeabi-v7a,而 eros 恰好不支持。因此我们把 eros 的 sdk 的 libs 里面的 armeabi 复制成 armeabi-v7a，然后在 gradle.build 里面添加 armeabi-v7a，最后就可以成功运行了。
