package com.marcesoft.traingo.gui.modelos;

import com.marcesoft.traingo.aplicacion.Estacion;
import com.marcesoft.traingo.aplicacion.FachadaAplicacion;
import org.jetbrains.annotations.NotNull;
import com.marcesoft.traingo.util.Ortograficos;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de desplegable de ubicaciones.
 */
public final class ModeloDesplegableUbicacion implements ComboBoxModel<String>, Cloneable {
    private final List<Estacion> ubicaciones;
    private Estacion selectedItem;

    /**
     * Constructor de la clase ModeloDesplegableUbicacion.
     * @param fa Fachada de la aplicación
     */
    public ModeloDesplegableUbicacion(@NotNull FachadaAplicacion fa) {
        this();
        this.ubicaciones.addAll(fa.getEstaciones());
        this.triggerRefresh();
//        System.out.println("Ubicaciones: " + this.ubicaciones);
    }

    /**
     * Constructor de la clase ModeloDesplegableUbicacion.
     */
    private ModeloDesplegableUbicacion() {
        super();
        this.ubicaciones = new ArrayList<>();
    }

    /**
     * Añade una ubicación al modelo de desplegable de ubicaciones.
     * @param ubicacion Ubicación a añadir
     */
    public void addUbicacion(String ubicacion) {
        this.ubicaciones.add(new Estacion(ubicacion));
        this.triggerRefresh();
    }

    @Override
    public Object getSelectedItem() {
        return this.selectedItem == null ? " " : this.selectedItem.ciudad();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for (Estacion ubicacion : ubicaciones) {
            if (ubicacion.ciudad().equals(anItem)) {
                this.selectedItem = ubicacion;
                return;
            }
        }
    }

    @Override
    public int getSize() {
        return this.ubicaciones.size();
    }

    @Override
    public String getElementAt(int index) {
        return this.ubicaciones.get(index).ciudad();
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        // No necesario (de momento)
        // Habria que implementar todo el patrón de observador
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        // No necesario (de momento)
        // Habria que implementar todo el patrón de observador
    }

    /**
     * Clona el modelo de desplegable de ubicaciones.
     *
     * @return Devuelve una copia del modelo de desplegable de ubicaciones.
     * @see Cloneable
     * @see #triggerRefresh()
     */
    @NotNull
    @Override
    public ModeloDesplegableUbicacion clone() {
        try {
            //triggerRefresh() o .ordenar() no es necesario, ya se ordena al añadir
            return (ModeloDesplegableUbicacion) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }

    }

    /**
     * Ordena las ubicaciones alfabéticamente. Expandible según necesidades.
     */
    private void triggerRefresh() {
        this.ubicaciones.sort(Ortograficos::Wrap2EstacionCompararAlfabeticamente);
    }


}
