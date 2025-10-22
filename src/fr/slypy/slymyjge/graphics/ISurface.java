package fr.slypy.slymyjge.graphics;


public interface ISurface {

    void bind();
    void unbind();
    int getFboId();
    int getTextureId();
    int getWidth();
    int getHeight();
    void free();
    
}
