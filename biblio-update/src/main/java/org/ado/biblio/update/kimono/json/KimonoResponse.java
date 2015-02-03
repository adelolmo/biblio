package org.ado.biblio.update.kimono.json;

import com.google.gson.annotations.SerializedName;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Andoni del Olmo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * @author Andoni del Olmo,
 * @since 25.01.15
 */
public class KimonoResponse {

    private String name;
    private int count;
    private int version;

    @SerializedName("results")
    private KimonoRelease kimonoRelease;

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

    public KimonoRelease getKimonoRelease() {
        return kimonoRelease;
    }

    public void setKimonoRelease(KimonoRelease kimonoRelease) {
        this.kimonoRelease = kimonoRelease;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KimonoResponse that = (KimonoResponse) o;

        if (count != that.count) return false;
        if (version != that.version) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (kimonoRelease != null ? !kimonoRelease.equals(that.kimonoRelease) : that.kimonoRelease != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = name != null ? name.hashCode() : 0;
        result1 = 31 * result1 + count;
        result1 = 31 * result1 + version;
        result1 = 31 * result1 + (kimonoRelease != null ? kimonoRelease.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KimonoResponse{");
        sb.append("name='").append(name).append('\'');
        sb.append(", count=").append(count);
        sb.append(", version=").append(version);
        sb.append(", result=").append(kimonoRelease);
        sb.append('}');
        return sb.toString();
    }
}