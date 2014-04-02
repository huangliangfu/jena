/*
 * Copyright 2013 YarcData LLC All Rights Reserved.
 */

package com.yarcdata.urika.hadoop.rdf.io.input;

import java.io.IOException;
import java.io.Writer;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.yarcdata.urika.hadoop.rdf.types.TripleWritable;

/**
 * Abstract tests for Triple input formats
 * 
 * @author rvesse
 * 
 */
public abstract class AbstractWholeFileTripleInputFormatTests extends AbstractNodeTupleInputFormatTests<Triple, TripleWritable> {

    @Override
    protected boolean canSplitInputs() {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    private void writeTuples(Model m, Writer writer) {
        RDFDataMgr.write(writer, m, this.getRdfLanguage());
    }
    
    /**
     * Gets the RDF language to write out generate tuples in
     * @return RDF language
     */
    protected abstract Lang getRdfLanguage();
    
    @Override
    protected final void generateTuples(Writer writer, int num) throws IOException {
        Model m = ModelFactory.createDefaultModel();
        Resource currSubj = m.createResource("http://example.org/subjects/0");
        Property predicate = m.createProperty("http://example.org/predicate");
        for (int i = 0; i < num; i++) {
            if (i % 10 == 0) {
                currSubj = m.createResource("http://example.org/subjects/" + (i / 10));
            }
            m.add(currSubj, predicate, m.createTypedLiteral(i));
        }
        this.writeTuples(m, writer);
        writer.close();
    }
    
    @Override
    protected final void generateMixedTuples(Writer writer, int num) throws IOException {
        // Write good data
        Model m = ModelFactory.createDefaultModel();
        Resource currSubj = m.createResource("http://example.org/subjects/0");
        Property predicate = m.createProperty("http://example.org/predicate");
        for (int i = 0; i < num / 2; i++) {
            if (i % 10 == 0) {
                currSubj = m.createResource("http://example.org/subjects/" + (i / 10));
            }
            m.add(currSubj, predicate, m.createTypedLiteral(i));
        }
        this.writeTuples(m, writer);
        
        // Write junk data
        for (int i = 0; i < num / 2; i++) {
            writer.write("junk data\n");
        }
        
        writer.flush();
        writer.close();
    }

    @Override
    protected final void generateBadTuples(Writer writer, int num) throws IOException {
        for (int i = 0; i < num; i++) {
            writer.write("junk data\n");
        }
        writer.flush();
        writer.close();
    }
}
