/*
  This file is licensed to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package net.sf.xmlunit.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.xmlunit.exceptions.ConfigurationException;
import net.sf.xmlunit.exceptions.XMLUnitException;
import org.xml.sax.InputSource;

/**
 * Conversion methods.
 */
public final class Convert {
    private Convert() { }

    /**
     * Creates a SAX InputSource from a TraX Source.
     *
     * <p>May use an XSLT identity transformation if SAXSource cannot
     * convert it directly.</p>
     */
    public static InputSource toInputSource(Source s) {
        try {
            InputSource is = SAXSource.sourceToInputSource(s);
            if (is == null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                StreamResult r = new StreamResult(bos);
                TransformerFactory fac = TransformerFactory.newInstance();
                Transformer t = fac.newTransformer();
                t.transform(s, r);
                s = new StreamSource(new ByteArrayInputStream(bos
                                                              .toByteArray()));
                is = SAXSource.sourceToInputSource(s);
            }
            return is;
        } catch (javax.xml.transform.TransformerConfigurationException e) {
            throw new ConfigurationException(e);
        } catch (javax.xml.transform.TransformerException e) {
            throw new XMLUnitException(e);
        }
    }
}