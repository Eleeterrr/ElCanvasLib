package eleeter.elcanvas.api;


public interface Fill
{

    int TYPE_SOLID = 0;
    int TYPE_LINEAR_GRADIENT = 1;
    int TYPE_RADIAL_GRADIENT = 2;

    int getFillType();

    /**
     * Primary base color
     */
    Color getPrimaryColor();

    void bindFill(UniformBinder binder);
}
