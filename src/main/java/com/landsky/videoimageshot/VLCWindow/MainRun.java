package com.landsky.videoimageshot.VLCWindow;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class MainRun {
    public static void main(String[] args) {
        NativeLibrary.addSearchPath(
                RuntimeUtil.getLibVlcLibraryName(), "D:\\VLC64"); //导入的路径是vlc的安装路径
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
    }
}
