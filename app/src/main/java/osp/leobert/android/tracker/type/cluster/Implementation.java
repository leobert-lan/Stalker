/*
 * MIT License
 *
 * Copyright (c) 2017 leobert-lan
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
 *
 */

package osp.leobert.android.tracker.type.cluster;


import java.lang.reflect.Field;

import osp.leobert.android.tracker.type.SupposeType;

/**
 * <p><b>Package:</b> osp.leobert.android.savedstate.type.cluster </p>
 * <p><b>Project:</b> MyJava </p>
 * <p><b>Classname:</b> Implementation </p>
 * <p><b>Description:</b> implementation class of one interface class </p>
 * Created by leobert on 2017/11/19.
 */

public class Implementation implements SupposeType {
    private final Class<?> typeInterface;

    public Implementation(Class<?> typeInterface) {
        if (!typeInterface.isInterface())
            throw new IllegalArgumentException("typeInterface should be interface");
        this.typeInterface = typeInterface;
    }

    @Override
    public boolean canBeChecked() {
        return true;
    }

    @Override
    public boolean check(Field field) {
        if (field == null)
            return false;

        return typeInterface.isAssignableFrom(field.getType());
    }

}
