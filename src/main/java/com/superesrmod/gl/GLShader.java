package com.superesrmod.gl;
import org.lwjgl.opengl.GL20;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
public class GLShader {
    private final int program;
    private final Map<String, Integer> uniformCache = new HashMap<>();
    private static final String VERT = "#version 150\nout vec2 texCoord;\nvoid main(){\nvec2 pos=vec2((gl_VertexID==1)?3.0:-1.0,(gl_VertexID==2)?3.0:-1.0);\ntexCoord=pos*0.5+0.5;\ngl_Position=vec4(pos,0.0,1.0);\n}";
    public GLShader(String fragmentResourcePath) {
        int vs = compile(GL20.GL_VERTEX_SHADER, VERT);
        int fs = compile(GL20.GL_FRAGMENT_SHADER, loadResource(fragmentResourcePath));
        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vs); GL20.glAttachShader(program, fs);
        GL20.glLinkProgram(program);
        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == 0)
            com.superesrmod.SuperESRMod.LOGGER.error("[GLShader] link failed: {}", GL20.glGetProgramInfoLog(program));
        GL20.glDeleteShader(vs); GL20.glDeleteShader(fs);
    }
    private static int compile(int type, String source) {
        int s = GL20.glCreateShader(type);
        GL20.glShaderSource(s, source); GL20.glCompileShader(s);
        if (GL20.glGetShaderi(s, GL20.GL_COMPILE_STATUS) == 0)
            com.superesrmod.SuperESRMod.LOGGER.error("[GLShader] compile: {}", GL20.glGetShaderInfoLog(s));
        return s;
    }
    private static String loadResource(String path) {
        try (InputStream is = GLShader.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) return "#version 150\nout vec4 fragColor;void main(){fragColor=vec4(1,0,1,1);}";
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) { return "#version 150\nout vec4 fragColor;void main(){fragColor=vec4(1,0,1,1);}"; }
    }
    public void bind() { GL20.glUseProgram(program); }
    public static void unbind() { GL20.glUseProgram(0); }
    private int loc(String n) { return uniformCache.computeIfAbsent(n, k -> GL20.glGetUniformLocation(program, k)); }
    public void setUniform(String n, float v) { int l=loc(n); if(l>=0) GL20.glUniform1f(l,v); }
    public void setUniform(String n, float a, float b) { int l=loc(n); if(l>=0) GL20.glUniform2f(l,a,b); }
    public void setUniform(String n, int v) { int l=loc(n); if(l>=0) GL20.glUniform1i(l,v); }
    public void setTexture(String name, int unit, int texId) { GL20.glActiveTexture(GL20.GL_TEXTURE0+unit); GL20.glBindTexture(GL20.GL_TEXTURE_2D, texId); setUniform(name, unit); }
    public void destroy() { if (program!=0) GL20.glDeleteProgram(program); }
}
