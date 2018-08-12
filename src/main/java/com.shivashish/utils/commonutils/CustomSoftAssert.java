package com.shivashish.utils.commonutils;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Maps;

import java.util.Iterator;
import java.util.Map;

public class CustomSoftAssert extends Assertion {
	private Map<AssertionError, IAssert> m_errors = Maps.newLinkedHashMap();

	public CustomSoftAssert() {
	}

	public void executeAssert(IAssert a) {
		try {
			a.doAssert();
		} catch (AssertionError var3) {
			this.m_errors.put(var3, a);
		}

	}

	public void assertAll() {
		if (!this.m_errors.isEmpty()) {
			StringBuilder sb = new StringBuilder("The following asserts failed:\n");
			boolean first = true;

			Map.Entry ae;
			for (Iterator i$ = this.m_errors.entrySet().iterator(); i$.hasNext(); sb.append(((IAssert) ae.getValue()).getMessage())) {
				ae = (Map.Entry) i$.next();
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
			}

			throw new AssertionError(sb.toString());
		}
	}

	public String getAssertAllMessage() {
		StringBuilder sb = new StringBuilder("The following asserts failed:\n");
		if (!this.m_errors.isEmpty()) {
			boolean first = true;

			Map.Entry ae;
			for (Iterator i$ = this.m_errors.entrySet().iterator(); i$.hasNext(); sb.append(((IAssert) ae.getValue()).getMessage())) {
				ae = (Map.Entry) i$.next();
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
			}

		}
		return sb.toString();
	}
}
