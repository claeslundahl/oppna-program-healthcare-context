/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

/**
 * 
 */
package se.vgregion.portal.auditlog;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.portlet.MockPortletRequest;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class AuditLogInfoContainerFactoryImplTest {

    private AuditLogInfoContainerFactory factory;

    @Mock
    RequestResponseConverter converter;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        factory = new AuditLogInfoContainerFactoryImpl();
        factory.setRequestResponseConverter(converter);

    }

    /**
     * Test method for
     * {@link se.vgregion.portal.auditlog.AuditLogInfoContainerFactoryImpl#getAuditLogInfoContainer(java.lang.String, java.lang.String, javax.portlet.PortletRequest)}
     * .
     */
    @Test
    public final void testGetAuditLogInfoContainer() {
        MockPortletRequest portletRequest = new MockPortletRequest();

        Map<String, String> uInfoMap = new HashMap<String, String>();
        uInfoMap.put(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString(), "remoteUid");
        portletRequest.setAttribute(PortletRequest.USER_INFO, uInfoMap);

        MockHttpServletRequest httpRequest = new MockHttpServletRequest();
        httpRequest.setRemoteAddr("127.0.0.1");
        httpRequest.setRemoteHost("remoteHost");
        httpRequest.setRemotePort(123);

        given(converter.getHttpServletRequest(portletRequest)).willReturn(httpRequest);

        AuditLogInfoContainer container = factory.getAuditLogInfoContainer("patientId", "searcherId",
                portletRequest);

        assertEquals("patientId", container.getPatientId());
        assertEquals("searcherId", container.getSearcherId());
        assertEquals("remoteUid", container.getRemoteUser());
        assertEquals("127.0.0.1", container.getRemoteIpAddress());
        assertEquals("remoteHost", container.getRemoteHost());
        assertEquals(123, container.getRemotePort());
    }
}
