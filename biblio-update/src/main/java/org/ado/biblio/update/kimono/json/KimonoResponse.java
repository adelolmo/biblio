package org.ado.biblio.update.kimono.json;

import com.google.gson.annotations.SerializedName;

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class KimonoResponse {

    private String name;
    private int count;
    private int version;

    @SerializedName("results")
    private Result result;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KimonoResponse that = (KimonoResponse) o;

        if (count != that.count) return false;
        if (version != that.version) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = name != null ? name.hashCode() : 0;
        result1 = 31 * result1 + count;
        result1 = 31 * result1 + version;
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KimonoResponse{");
        sb.append("name='").append(name).append('\'');
        sb.append(", count=").append(count);
        sb.append(", version=").append(version);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }
}