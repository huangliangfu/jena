/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package com.yarcdata.urika.hadoop.rdf.mapreduce.characteristics;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Assert;
import org.junit.Test;

import com.hp.hpl.jena.graph.NodeFactory;
import com.yarcdata.urika.hadoop.rdf.mapreduce.AbstractMapReduceTests;
import com.yarcdata.urika.hadoop.rdf.mapreduce.characteristics.CharacteristicSetReducer;
import com.yarcdata.urika.hadoop.rdf.types.CharacteristicSetWritable;
import com.yarcdata.urika.hadoop.rdf.types.CharacteristicWritable;

/**
 * Abstract tests for the {@link CharacteristicSetReducer}
 * 
 * @author rvesse
 */
public class CharacteristicSetReducerTest
        extends
        AbstractMapReduceTests<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, NullWritable> {

    @Override
    protected final Mapper<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable> getMapperInstance() {
        // Identity mapper
        return new Mapper<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable>();
    }

    @Override
    protected final Reducer<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, NullWritable> getReducerInstance() {
        return new CharacteristicSetReducer();
    }

    /**
     * Creates a set consisting of the given predicates
     * 
     * @param predicates
     *            Predicates
     * @return Set
     */
    protected CharacteristicSetWritable createSet(
            MapReduceDriver<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, NullWritable> driver,
            int inputOccurrences, int outputOccurrences, String... predicates) {
        CharacteristicSetWritable set = new CharacteristicSetWritable();
        for (String predicateUri : predicates) {
            set.add(new CharacteristicWritable(NodeFactory.createURI(predicateUri)));
        }
        for (int i = 1; i <= inputOccurrences; i++) {
            driver.addInput(set, set);
        }
        for (int i = 1; i <= outputOccurrences; i++) {
            driver.addOutput(set, NullWritable.get());
        }
        return set;
    }

    /**
     * Test characteristic set reduction
     * 
     * @throws IOException
     */
    @Test
    public void characteristic_set_reducer_01() throws IOException {
        MapReduceDriver<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, NullWritable> driver = this
                .getMapReduceDriver();

        this.createSet(driver, 1, 1, "http://predicate");

        driver.runTest(false);
    }

    /**
     * Test characteristic set reduction
     * 
     * @throws IOException
     */
    @Test
    public void characteristic_set_reducer_02() throws IOException {
        MapReduceDriver<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, NullWritable> driver = this
                .getMapReduceDriver();

        this.createSet(driver, 2, 1, "http://predicate");

        driver.runTest(false);

        List<Pair<CharacteristicSetWritable, NullWritable>> results = driver.run();
        CharacteristicSetWritable cw = results.get(0).getFirst();
        Assert.assertEquals(2, cw.getCount().get());
    }

    /**
     * Test characteristic set reduction
     * 
     * @throws IOException
     */
    @Test
    public void characteristic_set_reducer_03() throws IOException {
        MapReduceDriver<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, NullWritable> driver = this
                .getMapReduceDriver();

        this.createSet(driver, 1, 1, "http://predicate");
        this.createSet(driver, 1, 1, "http://other");

        driver.runTest(false);
    }

    /**
     * Test characteristic set reduction
     * 
     * @throws IOException
     */
    @Test
    public void characteristic_set_reducer_04() throws IOException {
        MapReduceDriver<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, NullWritable> driver = this
                .getMapReduceDriver();

        this.createSet(driver, 2, 1, "http://predicate");
        this.createSet(driver, 1, 1, "http://other");

        driver.runTest(false);

        List<Pair<CharacteristicSetWritable, NullWritable>> results = driver.run();
        for (Pair<CharacteristicSetWritable, NullWritable> pair : results) {
            CharacteristicSetWritable cw = pair.getFirst();
            boolean expectTwo = cw.getCharacteristics().next().getNode().get().hasURI("http://predicate");
            Assert.assertEquals(expectTwo ? 2 : 1, cw.getCount().get());
        }
    }

    /**
     * Test characteristic set reduction
     * 
     * @throws IOException
     */
    @Test
    public void characteristic_set_reducer_05() throws IOException {
        MapReduceDriver<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, NullWritable> driver = this
                .getMapReduceDriver();

        this.createSet(driver, 1, 1, "http://predicate", "http://other");
        this.createSet(driver, 1, 1, "http://other");

        driver.runTest(false);
    }

    /**
     * Test characteristic set reduction
     * 
     * @throws IOException
     */
    @Test
    public void characteristic_set_reducer_06() throws IOException {
        MapReduceDriver<CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, CharacteristicSetWritable, NullWritable> driver = this
                .getMapReduceDriver();

        this.createSet(driver, 2, 1, "http://predicate", "http://other");
        this.createSet(driver, 1, 1, "http://other");

        driver.runTest(false);

        List<Pair<CharacteristicSetWritable, NullWritable>> results = driver.run();
        for (Pair<CharacteristicSetWritable, NullWritable> pair : results) {
            CharacteristicSetWritable cw = pair.getFirst();
            boolean expectTwo = cw.hasCharacteristic("http://predicate");
            Assert.assertEquals(expectTwo ? 2 : 1, cw.getCount().get());
        }
    }
}
