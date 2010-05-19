/*
 * (c) Copyright 2010 Talis Systems Ltd.
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.riot;

import org.junit.Test ;

import com.hp.hpl.jena.graph.Node ;
import com.hp.hpl.jena.sparql.sse.SSE ;

public class TestChecker
{
    private static class ExFatal extends RuntimeException {} ;
    private static class ExError extends RuntimeException {} ;
    private static class ExWarning extends RuntimeException {} ;
    
    static ErrorHandler handler = new ErrorHandler()
    {
        public void error(String message, long line, long col)
        { throw new ExError() ; }

        public void fatal(String message, long line, long col)
        { throw new ExFatal() ; }

        public void warning(String message, long line, long col)
        { throw new ExWarning() ; }

    } ;
    static Checker checker = new Checker(handler) ;
    
//    boolean b ;
//
//    @Before
//    public void before()
//    {
//        b = JenaParameters.enableWhitespaceCheckingOfTypedLiterals ;
//        JenaParameters.enableWhitespaceCheckingOfTypedLiterals = false ;
//    }
//
//    @After
//    public void after()
//    {
//        JenaParameters.enableWhitespaceCheckingOfTypedLiterals = b ;
//    }
    
    @Test public void checker01() { check("''") ; }
    @Test public void checker02() { check("''@en") ; }
    @Test public void checker03() { check("<x>") ; }
    
    @Test (expected=ExWarning.class) public void checker10() { check("''^^xsd:dateTime") ; }

    // Whitespace facet processing means that these are legal.
    //--@Test public void checker11() { check("'  2010-05-19T01:01:01.01+0100'^^xsd:dateTime") ; }
  //--@Test public void checker12() { check("'\\n2010-05-19T01:01:01.01+0100'^^xsd:dateTime") ; }
    @Test public void checker13() { check("' 123'^^xsd:integer") ; }
    
    // Internal white space - illegal
    @Test (expected=ExWarning.class) public void checker14() { check("'12 3'^^xsd:integer") ; }
    @Test public void checker15() { check("'\\n123'^^xsd:integer") ; }

    @Test public void checker16() { check("'123.0  '^^xsd:float") ; }
    @Test public void checker17() { check("'123.0\\n'^^xsd:double") ; }

    
    // Other bad lexical forms.
    @Test(expected=ExWarning.class) public void checker20() { check("'XYZ'^^xsd:integer") ; }
    // Lang tag
    @Test(expected=ExWarning.class) public void checker21() { check("'XYZ'@abcdefghijklmn") ; }
    
    
    @Test(expected=ExWarning.class) public void checker30() { check("<http://base/[]iri>") ; }
    
    //Bad IRI

    
    //@Test public void checker12() { check("''@en") ; }
    
    
    

    private static void check(String string)
    {
        Node n = SSE.parseNode(string) ;
        checker.check(n, -1, -1) ;
    }
    
}

/*
 * (c) Copyright 2010 Talis Systems Ltd.
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