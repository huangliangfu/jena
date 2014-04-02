/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package com.yarcdata.urika.hadoop.rdf.io.output.writers;

import java.io.IOException;
import java.io.Writer;

import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yarcdata.urika.hadoop.rdf.types.AbstractNodeTupleWritable;

/**
 * An abstract implementation of a record writer that writes records in batches.
 * <p>
 * It is important to note that the writer will write output periodically once
 * sufficient tuples have been gathered. If there is an incomplete batch when
 * the {@link #close(TaskAttemptContext)} method is called then the final batch
 * will be written then. Writing in batches increases the chances that the
 * writer will be able to effectively use the syntax compressions of the RDF
 * serialization being used.
 * </p>
 * <p>
 * The implementation only writes the value portion of the key value pair since
 * it is the value portion that is used to convey the node tuples
 * </p>
 * 
 * @author rvesse
 * 
 * @param <TKey>
 * @param <TValue>
 * @param <T>
 */
public abstract class AbstractBatchedNodeTupleWriter<TKey, TValue, T extends AbstractNodeTupleWritable<TValue>> extends
        RecordWriter<TKey, T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBatchedNodeTupleWriter.class);

    private Writer writer;
    private long batchSize;

    protected AbstractBatchedNodeTupleWriter(Writer writer, long batchSize) {
        if (writer == null)
            throw new NullPointerException("writer cannot be null");
        if (batchSize <= 0)
            throw new IllegalArgumentException("batchSize must be >= 1");
        this.writer = writer;
        this.batchSize = batchSize;
    }

    @Override
    public final void write(TKey key, T value) throws IOException, InterruptedException {
        LOG.debug("write({}={})", key, value);
        if (this.add(value) >= this.batchSize) {
            long size = this.writeOutput(writer);
            if (size > 0)
                throw new IOException("Derived implementation failed to empty the current batch after writing");
        }
    }

    /**
     * Adds the tuple to the batch of tuples that will be written when the batch
     * threshold is reached or when the {@link #close(TaskAttemptContext)}
     * method is called.
     * 
     * @param value
     *            Tuple
     * @return The current size of the batch waiting to be written
     */
    protected abstract long add(T value);

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        if (this.writer != null) {
            long size = this.writeOutput(writer);
            if (size > 0)
                throw new IOException("Derived implementation failed to empty the current batch after writing");
            this.writer.close();
            this.writer = null;
        }
    }

    /**
     * Writes the current batch of tuples to the writer, the writer should not
     * be closed and the batch should be emptied by the implementation.
     * <p>
     * If the current batch is empty then this should be a no-op
     * </p>
     * 
     * @param writer
     *            Writer
     * @return Current batch size which should always be zero
     */
    protected abstract long writeOutput(Writer writer);

}
