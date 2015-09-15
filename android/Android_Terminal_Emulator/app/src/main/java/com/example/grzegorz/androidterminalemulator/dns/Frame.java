package com.example.grzegorz.androidterminalemulator.dns;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gpietrus on 01.08.15.
 */
abstract public class Frame {

    protected FrameHeader frameHeader;
    protected List<FramePayload> framePayloadList;

    public byte[] getBytes() throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] frameHeaderBytes = frameHeader.getBytes();
        byteArrayOutputStream.write(frameHeaderBytes);

        for (FramePayload framePayload : framePayloadList) {
            byte[] payloadBytes = framePayload.getBytes();
            byteArrayOutputStream.write(payloadBytes);
        }

        //concating
        return byteArrayOutputStream.toByteArray();
    }

}
