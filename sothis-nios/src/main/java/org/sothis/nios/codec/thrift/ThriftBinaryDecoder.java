package org.sothis.nios.codec.thrift;

import java.util.List;

import org.sothis.nios.ChannelContext;
import org.sothis.nios.ReadBuffer;
import org.sothis.nios.codec.Decoder;

public class ThriftBinaryDecoder extends Decoder {

	@Override
	protected boolean decode(ChannelContext ctx, ReadBuffer in, List<Object> out) {
		if(in.remaining()>4){
			
		}
		return false;
	}

}
