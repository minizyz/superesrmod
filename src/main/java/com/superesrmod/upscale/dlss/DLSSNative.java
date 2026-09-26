package com.superesrmod.upscale.dlss;

public final class DLSSNative {
    private DLSSNative() {}
    public static native long nvCreateContext(int outW, int outH, int inW, int inH);
    public static native void nvExecute(long ctx, int colorTex, int depthTex, int mvTex,
                                        int outTex, int inW, int inH, int outW, int outH,
                                        float jitterX, float jitterY, int frameIdx);
    public static native void nvGetRecommendedResolutionRange(long ctx, int[] outMin, int[] outMax);
    public static native void nvDestroyContext(long ctx);
    public static native boolean nvIsDLSSSupported();
}
