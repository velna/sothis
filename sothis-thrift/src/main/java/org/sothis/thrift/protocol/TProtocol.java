/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.sothis.thrift.protocol;

import java.nio.ByteBuffer;

import org.sothis.thrift.TException;

/**
 * Protocol interface definition.
 * 
 */
public abstract class TProtocol {

	protected final ByteBuffer buffer;

	public TProtocol(ByteBuffer buffer) {
		super();
		this.buffer = buffer;
	}

	abstract public void reset();

	public void skip(byte type) throws TException {
		switch (type) {
		case TType.BOOL:
			readBool();
			break;

		case TType.BYTE:
			readByte();
			break;

		case TType.I16:
			readI16();
			break;

		case TType.I32:
			readI32();
			break;

		case TType.I64:
			readI64();
			break;

		case TType.DOUBLE:
			readDouble();
			break;

		case TType.STRING:
			readBinary();
			break;

		case TType.STRUCT:
			readStructBegin();
			while (true) {
				TField field = readFieldBegin();
				if (field.type == TType.STOP) {
					break;
				}
				skip(field.type);
				readFieldEnd();
			}
			readStructEnd();
			break;

		case TType.MAP:
			TMap map = readMapBegin();
			for (int i = 0; i < map.size; i++) {
				skip(map.keyType);
				skip(map.valueType);
			}
			readMapEnd();
			break;

		case TType.SET:
			TSet set = readSetBegin();
			for (int i = 0; i < set.size; i++) {
				skip(set.elemType);
			}
			readSetEnd();
			break;

		case TType.LIST:
			TList list = readListBegin();
			for (int i = 0; i < list.size; i++) {
				skip(list.elemType);
			}
			readListEnd();
			break;

		default:
			break;
		}
	}

	/**
	 * Writing methods.
	 */

	abstract public void writeMessageBegin(TMessage message) throws TException;

	abstract public void writeMessageEnd() throws TException;

	abstract public void writeStructBegin(TStruct struct) throws TException;

	abstract public void writeStructEnd() throws TException;

	abstract public void writeFieldBegin(TField field) throws TException;

	abstract public void writeFieldEnd() throws TException;

	abstract public void writeFieldStop() throws TException;

	abstract public void writeMapBegin(TMap map) throws TException;

	abstract public void writeMapEnd() throws TException;

	abstract public void writeListBegin(TList list) throws TException;

	abstract public void writeListEnd() throws TException;

	abstract public void writeSetBegin(TSet set) throws TException;

	abstract public void writeSetEnd() throws TException;

	abstract public void writeBool(boolean b) throws TException;

	abstract public void writeByte(byte b) throws TException;

	abstract public void writeI16(short i16) throws TException;

	abstract public void writeI32(int i32) throws TException;

	abstract public void writeI64(long i64) throws TException;

	abstract public void writeDouble(double dub) throws TException;

	abstract public void writeString(String str) throws TException;

	abstract public void writeBinary(byte[] bs, int off, int length) throws TException;

	/**
	 * Reading methods.
	 */

	abstract public TMessage readMessageBegin() throws TException;

	abstract public void readMessageEnd() throws TException;

	abstract public TStruct readStructBegin() throws TException;

	abstract public void readStructEnd() throws TException;

	abstract public TField readFieldBegin() throws TException;

	abstract public void readFieldEnd() throws TException;

	abstract public TMap readMapBegin() throws TException;

	abstract public void readMapEnd() throws TException;

	abstract public TList readListBegin() throws TException;

	abstract public void readListEnd() throws TException;

	abstract public TSet readSetBegin() throws TException;

	abstract public void readSetEnd() throws TException;

	abstract public boolean readBool() throws TException;

	abstract public byte readByte() throws TException;

	abstract public short readI16() throws TException;

	abstract public int readI32() throws TException;

	abstract public long readI64() throws TException;

	abstract public double readDouble() throws TException;

	abstract public String readString() throws TException;

	abstract public byte[] readBinary() throws TException;

}
