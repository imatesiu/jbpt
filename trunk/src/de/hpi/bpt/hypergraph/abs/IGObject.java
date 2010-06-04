/**
 * Copyright (c) 2008 Artem Polyvyanyy
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.hpi.bpt.hypergraph.abs;

/**
 * Basic graph object interface
 * 
 * @author Artem Polyvyanyy
 */
public interface IGObject extends Comparable<IGObject> {
	/**
	 * Get unique identifier
	 * @return
	 */
	public String getId();

	/**
	 * Set unique identifier
	 * @param id Unique identifier
	 */
	public void setId(String id);
	
	/**
	 * Get graph object associated tag object
	 * @return Tag object
	 */
	public Object getTag();
	
	/**
	 * Set graph object associated tag object
	 * @param tag Tag object to set
	 */
	public void setTag(Object tag);
	
	/**
	 * Get name
	 * @return Name string
	 */
	public String getName();
	
	/**
	 * Set name
	 * @param name Name to set
	 */
	public void setName(String name);

	/**
	 * Get description
	 * @return Description string
	 */
	public String getDescription();
	
	/**
	 * Set description
	 * @param desc Description to set
	 */
	public void setDescription(String desc);
}