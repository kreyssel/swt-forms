/*
 * Copyright (c) 2002-2006 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package net.ffxml.swtforms.layout;

import java.io.Serializable;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

/**
 * Describes sizes as used by the {@link com.jgoodies.forms.layout.FormLayout}
 * that provide lower and upper bounds.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.2 $
 * @see Sizes
 * @see ConstantSize
 * @see Sizes.ComponentSize
 */

final class BoundedSize implements Size, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Holds the base size.
	 */
	private final Size basis;

	/**
	 * Holds an optional lower bound.
	 */
	private Size lowerBound;

	/**
	 * Holds an optional upper bound.
	 */
	private Size upperBound;

	// Instance Creation ****************************************************

	/**
	 * Constructs a BoundedSize for the given basis using the specified lower
	 * and upper bounds.
	 * 
	 * @param basis
	 *            the base size
	 * @param lowerBound
	 *            the lower bound size
	 * @param upperBound
	 *            the upper bound size
	 * @throws NullPointerException
	 *             if the basis is null
	 */
	BoundedSize(Size basis, Size lowerBound, Size upperBound) {
		if (basis == null)
			throw new NullPointerException(
					"The basis of a bounded size must not be null.");
		this.basis = basis;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	// Implementation of the Size Interface *********************************

	/**
	 * Returns this size as pixel size. Neither requires the component list nor
	 * the specified measures. Honors the lower and upper bound.
	 * <p>
	 * 
	 * Invoked by <code>FormSpec</code> to determine the size of a column or
	 * row.
	 * 
	 * @param composite
	 *            the layout container
	 * @param controls
	 *            the list of components to measure
	 * @param minMeasure
	 *            the measure used to determine the minimum size
	 * @param prefMeasure
	 *            the measure used to determine the preferred size
	 * @param defaultMeasure
	 *            the measure used to determine the default size
	 * @return the maximum size in pixels
	 * @see FormSpec#maximumSize(Composite, List, FormLayout.Measure,
	 *      FormLayout.Measure, FormLayout.Measure)
	 */
	public int maximumSize(Composite composite, List controls,
			FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure,
			FormLayout.Measure defaultMeasure) {
		int size = basis.maximumSize(composite, controls, minMeasure,
				prefMeasure, defaultMeasure);
		if (lowerBound != null) {
			size = Math.max(size, lowerBound.maximumSize(composite, controls,
					minMeasure, prefMeasure, defaultMeasure));
		}
		if (upperBound != null) {
			size = Math.min(size, upperBound.maximumSize(composite, controls,
					minMeasure, prefMeasure, defaultMeasure));
		}
		return size;
	}

	// Overriding Object Behavior *******************************************

	/**
	 * Indicates whether some other BoundedSize is "equal to" this one.
	 * 
	 * @param object
	 *            the object with which to compare
	 * @return <code>true</code> if this object is the same as the object
	 *         argument, <code>false</code> otherwise.
	 * @see Object#hashCode()
	 * @see java.util.Hashtable
	 */
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof BoundedSize))
			return false;
		BoundedSize size = (BoundedSize) object;
		return basis.equals(size.basis)
				&& ((lowerBound == null && size.lowerBound == null) || (lowerBound != null && lowerBound
						.equals(size.lowerBound)))
				&& ((upperBound == null && size.upperBound == null) || (upperBound != null && upperBound
						.equals(size.upperBound)));
	}

	/**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hashtables such as those provided by
	 * <code>java.util.Hashtable</code>.
	 * 
	 * @return a hash code value for this object.
	 * @see Object#equals(Object)
	 * @see java.util.Hashtable
	 */
	public int hashCode() {
		int hashValue = basis.hashCode();
		if (lowerBound != null) {
			hashValue = hashValue * 37 + lowerBound.hashCode();
		}
		if (upperBound != null) {
			hashValue = hashValue * 37 + upperBound.hashCode();
		}
		return hashValue;
	}

	/**
	 * Returns a string representation of this size object.
	 * 
	 * <strong>Note:</strong> The string representation may change at any time.
	 * It is strongly recommended to not use this string for parsing purposes.
	 * 
	 * @return a string representation of the constant size
	 */
	public String toString() {
		if (lowerBound != null) {
			return upperBound == null ? "max(" + basis + ';' + lowerBound + ')'
					: "max(" + lowerBound + ';' + "min(" + basis + ';'
							+ upperBound + "))";
		} else if (upperBound != null) {
			return "min(" + basis + ';' + upperBound + ')';
		} else {
			return "bounded(" + basis + ')';
		}

	}

}