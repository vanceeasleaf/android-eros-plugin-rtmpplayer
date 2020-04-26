package com.rtmpplayer.component;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

//import com.farwolf.weex.annotation.WeexComponent;
//import com.farwolf.weex.util.Const;
//import com.farwolf.weex.util.Weex;
//import com.taobao.weex.ui.action.BasicComponentData;

//import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;
//import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;

@WeexComponent(names = "rtmpplayer")
public class RtmpPlayer extends WXComponent<SurfaceView> {
    public final static String TAG = "MainActivity";
    private String mFilePath;
    private SurfaceView mSurface;
    private SurfaceHolder holder;
    private LibVLC libvlc;
    private MediaPlayer mMediaPlayer = null;
    private int mVideoWidth;
    private int mVideoHeight;
    public RtmpPlayer(WXSDKInstance instance, WXDomObject node, WXVContainer parent) {
        super(instance,node, parent);
        Log.d("ccc", "WXPVideo: xxxx");
    }

    @Override
    protected SurfaceView initComponentHostView(@NonNull Context context) {
        SurfaceView surfaceView=new SurfaceView(context);
        holder=surfaceView.getHolder();
        mSurface=surfaceView;

        return surfaceView;
    }






    @Override
    public void onActivityPause() {
        super.onActivityPause();
      //  this.pause();
        releasePlayer();
    }
    @Override
    public void  onActivityDestroy(){
        super.onActivityDestroy();
        releasePlayer();
    }
    @JSMethod
    @Override
    public void destroy() {
        super.destroy();
        // 进行自定义 Component 的必要销毁逻辑
        releasePlayer();
    }



    @WXComponentProp(name = "src")
    public void setSrc(String src)
    { Log.d("ccc", "setSrc: "+src);
        if(src.isEmpty())return;
        mFilePath=src;
        Log.d("ccc", "setSrc: "+src);
     //   mFilePath ="rtmp://58.200.131.2:1935/livetv/gxtv";// "rtmp://rrbalancer.broadcast.tneg.de:1935/pw/ruk/ruk";
        createPlayer(src);

    }



    /**
     * Creates MediaPlayer and plays video
     *
     * @param media
     */
    private void createPlayer(String media) {
        releasePlayer();
        try {
            if (media.length() > 0) {
                Toast toast = Toast.makeText(this.getContext(), media, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                        0);
                toast.show();
            }

            // Create LibVLC
            // TODO: make this more robust, and sync with audio demo
            ArrayList<String> options = new ArrayList<String>();
            //options.add("--subsdec-encoding <encoding>");
            options.add("--aout=opensles");
            options.add("--audio-time-stretch"); // time stretching
            options.add("-vvv"); // verbosity
            libvlc = new LibVLC(this.getContext(), options);
            holder.setKeepScreenOn(true);

            // Creating media player
            mMediaPlayer = new MediaPlayer(libvlc);
         //   mMediaPlayer.setEventListener(mPlayerListener);

            // Seting up video output
            final IVLCVout vout = mMediaPlayer.getVLCVout();
            vout.setVideoView(mSurface);
            //vout.setSubtitlesView(mSurfaceSubtitles);
            // vout.addCallback(this);
            vout.attachViews();

            Media m = new Media(libvlc, Uri.parse(media));
            mMediaPlayer.setMedia(m);
            mMediaPlayer.play();
        } catch (Exception e) {
            Toast.makeText(this.getContext(), "Error in creating player!", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void releasePlayer() {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
       // vout.removeCallback(this);
        vout.detachViews();
        holder = null;
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }








}
