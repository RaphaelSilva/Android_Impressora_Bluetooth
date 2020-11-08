/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.barbecue;

/**
 *
 * @author Raphael
 */
public class DimensionRsn {
	/**
	 * The width dimension; negative values can be used. 
	 *
	 * @serial
	 * @see #getSize
	 * @see #setSize
	 * @since 1.0
	 */
	private int width;

	/**
	 * The height dimension; negative values can be used. 
	 *
	 * @serial
	 * @see #getSize
	 * @see #setSize
	 * @since 1.0
	 */
	private int height;

	/** 
	 * Creates an instance of <code>Dimension</code> with a width 
	 * of zero and a height of zero. 
	 */
	public DimensionRsn() {
		this(0, 0);
	}

	/** 
	 * Creates an instance of <code>Dimension</code> whose width  
	 * and height are the same as for the specified dimension. 
	 *
	 * @param    d   the specified dimension for the 
	 *               <code>width</code> and 
	 *               <code>height</code> values
	 */
	public DimensionRsn(DimensionRsn d) {
		this(d.width, d.height);
	}

	/** 
	 * Constructs a <code>Dimension</code> and initializes
	 * it to the specified width and specified height.
	 *
	 * @param width the specified width 
	 * @param height the specified height
	 */
	public DimensionRsn(int width, int height) {
		this.width = width;
		this.height = height;
	}
	/**
	 * {@inheritDoc}
	 * @since 1.2
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * {@inheritDoc}
	 * @since 1.2
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Sets the size of this <code>Dimension</code> object to
	 * the specified width and height in double precision.
	 * Note that if <code>width</code> or <code>height</code>
	 * are larger than <code>Integer.MAX_VALUE</code>, they will
	 * be reset to <code>Integer.MAX_VALUE</code>.
	 *
	 * @param width  the new width for the <code>Dimension</code> object
	 * @param height the new height for the <code>Dimension</code> object
	 * @since 1.2
	 */
	public void setSize(double width, double height) {
		this.width = (int) Math.ceil(width);
		this.height = (int) Math.ceil(height);
	}

	/**
	 * Gets the size of this <code>Dimension</code> object.
	 * This method is included for completeness, to parallel the
	 * <code>getSize</code> method defined by <code>Component</code>.
	 *
	 * @return   the size of this dimension, a new instance of 
	 *           <code>Dimension</code> with the same width and height
	 * @see      java.awt.Dimension#setSize
	 * @see      java.awt.Component#getSize
	 * @since    1.1
	 */
	public DimensionRsn getSize() {
		return new DimensionRsn(width, height);
	}	

	/**
	 * Sets the size of this <code>Dimension</code> object to the specified size.
	 * This method is included for completeness, to parallel the
	 * <code>setSize</code> method defined by <code>Component</code>.
	 * @param    d  the new size for this <code>Dimension</code> object
	 * @see      java.awt.Dimension#getSize
	 * @see      java.awt.Component#setSize
	 * @since    1.1
	 */
	public void setSize(DimensionRsn d) {
		setSize(d.width, d.height);
	}	

	/**
	 * Sets the size of this <code>Dimension</code> object 
	 * to the specified width and height.
	 * This method is included for completeness, to parallel the
	 * <code>setSize</code> method defined by <code>Component</code>.
	 *
	 * @param    width   the new width for this <code>Dimension</code> object
	 * @param    height  the new height for this <code>Dimension</code> object
	 * @see      java.awt.Dimension#getSize
	 * @see      java.awt.Component#setSize
	 * @since    1.1
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}	
}
