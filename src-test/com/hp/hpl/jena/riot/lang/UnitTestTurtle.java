/*
 * (c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.riot.lang;


import atlas.io.PeekReader;

import java.io.InputStream;
import java.io.Reader;

import junit.framework.TestCase;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.riot.JenaReaderNTriples2;
import com.hp.hpl.jena.riot.JenaReaderTurtle2;
import com.hp.hpl.jena.riot.ParseException;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.FileUtils;

public class UnitTestTurtle extends TestCase
{
    String input ;
    String output ;
    String baseIRI ;
    
    public UnitTestTurtle(String name, String input, String output, String baseIRI)
    {
        super(name) ;
        this.input = input ;
        this.output = output ;
        this.baseIRI = baseIRI ;
    }
    
    @Override
    public void runTest()
    {
        Model model = ModelFactory.createDefaultModel() ;
        RDFReader t = new JenaReaderTurtle2() ;
        try {
            if ( baseIRI != null )
            {
                InputStream in =  FileManager.get().open(input) ;
                Reader r = PeekReader.makeUTF8(in) ;
                t.read(model, r, baseIRI) ;
            }
            else
                t.read(model, input) ;  
            // "http://www.w3.org/2001/sw/DataAccess/df1/tests/rdfq-results.ttl"

            String syntax = FileUtils.guessLang(output, FileUtils.langNTriple) ;
            
            //Model results = FileManager.get().loadModel(output, syntax);

            Model results = ModelFactory.createDefaultModel() ;
            // Supports \ U escapes
            // But the tokenizer had better be right! (they share the same tokenizer)
            new JenaReaderNTriples2().read(results, output) ;

            boolean b = model.isIsomorphicWith(results) ;
            if ( !b )
            {
                model.isIsomorphicWith(results) ;
                System.out.println("---- Input");
                model.write(System.out, "TTL") ;
                System.out.println("---- Output");
                results.write(System.out, "TTL") ;
                System.out.println("--------");
            }
            
            if ( !b )
                assertTrue("Models not isomorphic", b) ;
        } catch (ParseException ex)
        {
            // Catch and retrhow - debugging.
            throw ex ;    
        }
        catch (RuntimeException ex) 
        { 
            ex.printStackTrace(System.err) ;
            throw ex ; }
    }
}

/*
 * (c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */