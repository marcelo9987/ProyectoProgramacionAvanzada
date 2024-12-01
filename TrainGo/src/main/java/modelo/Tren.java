package modelo;

import java.util.UUID;

public record Tren(UUID id, int num) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tren tren)) {
            return false;
        }

        return id.equals(tren.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
