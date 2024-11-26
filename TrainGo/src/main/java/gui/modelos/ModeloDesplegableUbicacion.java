package gui.modelos;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ModeloDesplegableUbicacion implements ComboBoxModel<String> {
    private List<String> ubicaciones;
    private String selectedItem;

    public ModeloDesplegableUbicacion() {
        this.ubicaciones = new ArrayList<>();
    }

    public void addUbicacion(String ubicacion) {
        this.ubicaciones.add(ubicacion);
    }

    @Override
    public Object getSelectedItem() {
        return this.selectedItem;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for (String ubicacion : ubicaciones) {
            if (ubicacion.equals(anItem)) {
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
        return this.ubicaciones.get(index);
    }

    @Override
    public void addListDataListener(javax.swing.event.ListDataListener l) {
        // No necesario (de momento)
    }

    @Override
    public void removeListDataListener(javax.swing.event.ListDataListener l) {
        // No necesario (de momento)
    }

    public ModeloDesplegableUbicacion clone() {
        ModeloDesplegableUbicacion modelo = new ModeloDesplegableUbicacion();
        for (String ubicacion : this.ubicaciones) {
            modelo.addUbicacion(ubicacion);
        }
        return modelo;
    }


}
