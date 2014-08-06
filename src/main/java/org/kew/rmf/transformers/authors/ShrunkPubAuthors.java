/*
 * Copyright © 2012, 2013, 2014 Royal Botanic Gardens, Kew.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.kew.rmf.transformers.authors;

import org.apache.commons.lang3.StringUtils;
import org.kew.rmf.transformers.SafeStripNonAlphasTransformer;
import org.kew.rmf.transformers.StringShrinker;
import org.kew.rmf.transformers.Transformer;

/**
 * This transformer tries to identify all publication authors of plant names in
 * a string and returns a string where each of their surnames are shrunk/cropped
 * to a length of `shrinkTo`.
 *
 * For examples of standard usage and corner cases see {@link
 * ShrunkPubAuthorsTest}
 */
public class ShrunkPubAuthors implements Transformer {

    private Integer shrinkTo = null;

    @Override
    public String transform(String s) {
        s = new DotFDotCleaner().transform(s);
        s = new CleanedPubAuthors().transform(s);
        s = new SurnameExtractor().transform(s);
        s = new SafeStripNonAlphasTransformer().transform(s);
        s = s.replaceAll("\\s+", " ");
        // shrink each identified author surname to shrinkTo if set
        if (this.shrinkTo != null) {
            String[] toShrink = s.split(" ");
            for (int i=0;i<toShrink.length;i++) {
                toShrink[i] = new StringShrinker(this.shrinkTo).transform(toShrink[i]);
            }
            s = StringUtils.join(toShrink, " ");
        }
        return s.toLowerCase();
    }

    public Integer getShrinkTo() {
        return shrinkTo;
    }

    public void setShrinkTo(Integer shrinkTo) {
        this.shrinkTo = shrinkTo;
    }

}
