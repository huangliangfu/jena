/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package com.yarcdata.urika.hadoop.rdf.io.input.util;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link BlockInputStream}
 * 
 * @author rvesse
 * 
 */
public class BlockInputStreamTest extends AbstractTrackableInputStreamTests {

    @Override
    protected TrackableInputStream getInstance(InputStream input) {
        return new BlockInputStream(input, Long.MAX_VALUE);
    }

    /**
     * Gets an instance of a block input stream
     * 
     * @param input
     *            Underlying input stream
     * @param limit
     *            Limit on bytes to read
     * @return Block input stream
     */
    protected BlockInputStream getInstance(InputStream input, long limit) {
        return new BlockInputStream(input, limit);
    }
    
    protected final void testSingleByteRead(int length, long limit) throws IOException {
        InputStream input = this.generateData(length);
        TrackableInputStream trackable = this.getInstance(input, limit);
        long count = 0;
        while (trackable.read() >= 0) {
            count++;
        }
        int expected = (int) Math.min(length, limit);
        Assert.assertEquals(expected, count);
        Assert.assertEquals(expected, trackable.getBytesRead());
        trackable.close();
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_01() throws IOException {
        this.testSingleByteRead(0, 0);
    }

    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_02() throws IOException {
        this.testSingleByteRead(100, 0);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_03() throws IOException {
        this.testSingleByteRead(100, 50);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_04() throws IOException {
        this.testSingleByteRead(100, 100);
    }

    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_05() throws IOException {
        // 1KB
        this.testSingleByteRead(BYTES_PER_KB, 1);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_06() throws IOException {
        // 1KB
        this.testSingleByteRead(BYTES_PER_KB, 100);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_07() throws IOException {
        // 1KB
        this.testSingleByteRead(BYTES_PER_KB, BYTES_PER_KB / 2);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_08() throws IOException {
        // 1KB
        this.testSingleByteRead(BYTES_PER_KB, BYTES_PER_KB);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_09() throws IOException {
        // 1KB
        this.testSingleByteRead(BYTES_PER_KB, BYTES_PER_MB);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_10() throws IOException {
        // 1KB
        this.testSingleByteRead(BYTES_PER_KB, BYTES_PER_MB * 10);
    }

    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_11() throws IOException {
        // 1MB
        this.testSingleByteRead(BYTES_PER_MB, 1);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_12() throws IOException {
        // 1MB
        this.testSingleByteRead(BYTES_PER_MB, 100);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_13() throws IOException {
        // 1MB
        this.testSingleByteRead(BYTES_PER_MB, BYTES_PER_KB);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_14() throws IOException {
        // 1MB
        this.testSingleByteRead(BYTES_PER_MB, BYTES_PER_MB / 2);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_15() throws IOException {
        // 1MB
        this.testSingleByteRead(BYTES_PER_MB, BYTES_PER_MB);
    }
    
    /**
     * Test reading byte by byte
     * 
     * @throws IOException
     */
    @Test
    public final void block_input_read_single_16() throws IOException {
        // 1MB
        this.testSingleByteRead(BYTES_PER_MB, BYTES_PER_MB * 10);
    }

}
