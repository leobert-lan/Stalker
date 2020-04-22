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

package osp.leobert.android.burypoint.type;


import java.lang.reflect.Field;

import osp.leobert.android.burypoint.FieldReader;
import osp.leobert.android.burypoint.MethodReader;
import osp.leobert.android.burypoint.type.cluster.Implementation;
import osp.leobert.android.burypoint.type.cluster.NegativeSuppose;
import osp.leobert.android.burypoint.type.cluster.PrimitiveType;
import osp.leobert.android.burypoint.type.cluster.SimpleType;


/**
 * <p><b>Package:</b> osp.leobert.android.savedstate.type </p>
 * <p><b>Project:</b> MyJava </p>
 * <p><b>Classname:</b> Types </p>
 * <p><b>Description:</b> enum of type </p>
 * Created by leobert on 2017/11/15.
 */

public enum Type implements SupposeType {
    Infer(new NegativeSuppose(), FieldReader.DefaultReader.INSTANCE, MethodReader.DefaultReader.INSTANCE),

    PrimitiveMethod(new NegativeSuppose(), FieldReader.DefaultReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),


    Boolean(new PrimitiveType(boolean.class), FieldReader.BooleanReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    Byte(new PrimitiveType(byte.class), FieldReader.ByteReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    Char(new PrimitiveType(char.class), FieldReader.CharReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    Short(new PrimitiveType(short.class), FieldReader.ShortReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    Int(new PrimitiveType(int.class), FieldReader.IntReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    Long(new PrimitiveType(long.class), FieldReader.LongReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    Float(new PrimitiveType(float.class), FieldReader.FloatReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    Double(new PrimitiveType(double.class), FieldReader.DoubleReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    String(new SimpleType(java.lang.String.class), FieldReader.StringReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    BoxedBoolean(new SimpleType(Boolean.class), FieldReader.PrimitiveBoxedReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),
    BoxedByte(new SimpleType(Byte.class), FieldReader.PrimitiveBoxedReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),
    BoxedChar(new SimpleType(Character.class), FieldReader.PrimitiveBoxedReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),
    BoxedShort(new SimpleType(Short.class), FieldReader.PrimitiveBoxedReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),
    BoxedInt(new SimpleType(Integer.class), FieldReader.PrimitiveBoxedReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),
    BoxedLong(new SimpleType(Long.class), FieldReader.PrimitiveBoxedReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),
    BoxedFloat(new SimpleType(Float.class), FieldReader.PrimitiveBoxedReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),
    BoxedDouble(new SimpleType(Double.class), FieldReader.PrimitiveBoxedReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    CharSequence(new Implementation(java.lang.CharSequence.class), FieldReader.StringReader.INSTANCE, MethodReader.PrimitiveReader.INSTANCE),

    Null(new NegativeSuppose(), FieldReader.DefaultReader.INSTANCE, MethodReader.DefaultReader.INSTANCE),
    Object(new SupposeType() {
        @Override
        public boolean canBeChecked() {
            return true;
        }

        @Override
        public boolean check(Field field) {
            return true;
        }
    }, FieldReader.ObjectReader.INSTANCE, MethodReader.DefaultReader.INSTANCE);

//    Delegate(new SupposeType() {
//        @Override
//        public boolean canBeChecked() {
//            return true;
//        }
//
//        @Override
//        public boolean check(Field field) {
//            return true;
//        }
//    }, DelegateBoxWriter.getInstance(),
//            DelegateBoxReader.getInstance());


    private final SupposeType supposeType;


    private final FieldReader fieldReader;

    private final MethodReader methodReader;


//    Type(SupposeType supposeType, FieldReader fieldReader) {
//        this.supposeType = supposeType;
//        this.fieldReader = fieldReader;
//    }

    Type(SupposeType supposeType, FieldReader fieldReader, MethodReader methodReader) {
        this.supposeType = supposeType;
        this.fieldReader = fieldReader;
        this.methodReader = methodReader;
    }

    @Override
    public boolean canBeChecked() {
        return supposeType.canBeChecked();
    }

    @Override
    public boolean check(Field field) {
        if (field == null)
            return Null.equals(this);

        return supposeType.check(field);
    }

    public FieldReader getFieldReader() {
        return fieldReader;
    }

    public MethodReader getMethodReader() {
        return methodReader;
    }
}
