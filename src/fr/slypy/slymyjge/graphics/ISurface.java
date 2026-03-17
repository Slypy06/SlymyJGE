package fr.slypy.slymyjge.graphics;

public interface ISurface {

    void bind();
    void unbind();
    void rebind();
    int getFboId();
    int getTextureId();
    int getWidth();
    int getHeight();
    void free();
    
}
