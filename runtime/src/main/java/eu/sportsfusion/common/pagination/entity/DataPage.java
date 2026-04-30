package eu.sportsfusion.common.pagination.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.util.Collections;
import java.util.List;

/**
 * @author Anatoli Pranovich
 */
public class DataPage<E> {

    private List<E> data;
    private int number;
    private int size;
    private Long totalRecords;
    private Long totalPages;

    private boolean last;
    private boolean first;

    public DataPage(List<E> extendedData, int number, int size) {
        this.number = number;
        this.size = size;

        if (extendedData == null) {
            extendedData = Collections.emptyList();
        }
        last = (extendedData.size() <= size);
        first = (number == 0);
        if (last) {
            data = extendedData;
        } else {
            data = extendedData.subList(0, size);
        }
    }

    public List<E> getData() {
        return data;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public boolean isLast() {
        return last;
    }

    public boolean isFirst() {
        return first;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Long getTotalPages() {
        if (totalPages == null) {
            if (totalRecords != null && size > 0) {
                long tot = totalRecords / size;

                if ((tot == 0) || (tot * size < totalRecords)) {
                    tot++;
                }
                totalPages = tot;
            }
        }

        return totalPages;
    }

    public JsonObject asJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder().
                add("number", getNumber()).add("size", getSize()).add("first", isFirst()).add("last", isLast());

        if (getTotalRecords() != null) {
            builder.add("totalRecord", getTotalRecords());
        }
        if (getTotalPages() != null) {
            builder.add("totalPages", getTotalPages());
        }
        return Json.createObjectBuilder().add("page", builder).build();
    }
}
