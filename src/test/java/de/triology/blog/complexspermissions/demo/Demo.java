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
package de.triology.blog.complexspermissions.demo;

import de.triology.blog.complexspermissions.ComplexPermissionRealm;
import de.triology.blog.complexspermissions.FileOperation;
import de.triology.blog.complexspermissions.RequestedFileAccess;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static de.triology.blog.complexspermissions.PathUtils.filePath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Demo {

    private Subject subject;

    @BeforeClass
    public static void setUpShiro() throws Exception {
        Realm realm = new ComplexPermissionRealm();
        SecurityManager securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("user", "password"));
        assertTrue(subject.isAuthenticated());
    }

    @Before
    public void optainSubject() throws Exception {
        subject = SecurityUtils.getSubject();
    }

    @Test
    public void allowsPermittedAccessToAFile() throws Exception {
        RequestedFileAccess requestedFileAccess = createFileReadAccessRequest("departments/development/employee_987");
        assertTrue(subject.isPermitted(requestedFileAccess));
    }

    @Test
    public void doesNotAllowWriteAccessIdOnlyReadIsPermitted() throws Exception {
        RequestedFileAccess requestedFileAccess = createFileWriteAccessRequest("departments/development/employee_987");
        assertFalse(subject.isPermitted(requestedFileAccess));
    }

    @Test
    public void allowsReadAccessToAllAncestors() throws Exception {
        RequestedFileAccess requestedFileAccess = createFileReadAccessRequest("departments/development");
        assertTrue(subject.isPermitted(requestedFileAccess));
    }

    @Test
    public void allowsPermittedAccessToDescendantFile() throws Exception {
        RequestedFileAccess requestedFileAccess = createFileWriteAccessRequest("departments/finance/employee_123");
        assertTrue(subject.isPermitted(requestedFileAccess));
    }

    private RequestedFileAccess createFileReadAccessRequest(String path) {
        return createFileAccessRequest(path, FileOperation.READ);
    }

    private RequestedFileAccess createFileWriteAccessRequest(String path) {
        return createFileAccessRequest(path, FileOperation.WRITE);
    }

    private RequestedFileAccess createFileAccessRequest(String path, FileOperation fileOperation) {
        return new RequestedFileAccess(filePath(path), fileOperation);
    }
}