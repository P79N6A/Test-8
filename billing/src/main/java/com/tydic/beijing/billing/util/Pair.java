package com.tydic.beijing.billing.util;

import java.io.Serializable;

/**
 * 
 * @author Tian
 *
 * @param <A>
 * @param <B>
 */
public final class Pair<A, B> implements Serializable {
	private static final long serialVersionUID = 1L;
	public final A first;
	public final B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Pair<A, B> other = (Pair<A, B>) obj;
		if ((this.first != other.first)
				&& ((this.first == null) || !(this.first.equals(other.first)))) {
			return false;
		}
		if ((this.second != other.second)
				&& ((this.second == null) || !(this.second.equals(other.second)))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Pair Value[" + first.toString() + "," + second.toString() + "]";
	}
}
