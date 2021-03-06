/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 TRIOLOGY GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.triology.blog.complexspermissions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileRelations {

    private final Path file;

    private FileRelations(Path file) {
        this.file = file;
    }

    static FileRelations is(Path file) {
        return new FileRelations(file);
    }

    boolean descendantOf(Path potentialAncestor) {
        try {
            return isDescendantOf(potentialAncestor);
        } catch (IOException e) {
            // wrap checked exception to avoid cluttering up the example code
            throw new RuntimeException(e);
        }
    }

    private boolean isDescendantOf(Path potentialAncestor) throws IOException {
        return Files.walk(potentialAncestor)
                .anyMatch(descendant -> descendant.equals(this.file));
    }

    boolean theSameAs(Path otherFile) {
        return this.file.equals(otherFile);
    }

}
