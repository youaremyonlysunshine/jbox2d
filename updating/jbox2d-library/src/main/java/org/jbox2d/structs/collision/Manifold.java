/*
 * JBox2D - A Java Port of Erin Catto's Box2D
 * 
 * JBox2D homepage: http://jbox2d.sourceforge.net/ 
 * Box2D homepage: http://www.box2d.org
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package org.jbox2d.structs.collision;

import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;

// updated to rev 100
/**
 * A manifold for two touching convex shapes.
 * Box2D supports multiple types of contact:
 * <ul><li>clip point versus plane with radius</li>
 * <li>point versus point with radius (circles)</li></ul>
 * The local point usage depends on the manifold type:
 * <ul><li>e_circles: the local center of circleA</li>
 * <li>e_faceA: the center of faceA</li>
 * <li>e_faceB: the center of faceB</li></ul>
 * Similarly the local normal usage:
 * <ul><li>e_circles: not used</li>
 * <li>e_faceA: the normal on polygonA</li>
 * <li>e_faceB: the normal on polygonB</li></ul>
 * We store contacts in this way so that position correction can
 * account for movement, which is critical for continuous physics.
 * All contact scenarios must be expressed in one of these types.
 * This structure is stored across time steps, so we keep it small.
 */
public class Manifold {
	
	public static enum ManifoldType{
		CIRCLES,
		FACE_A,
		FACE_B
	}
	
	/** The points of contact. */
    public final ManifoldPoint[] points;
    
    /** not use for Type::e_points */
    public final Vec2 localNormal;
    
    /** usage depends on manifold type */
    public final Vec2 localPoint;
    
    public ManifoldType type;
    
    /** The number of manifold points. */
    public int pointCount;

    /**
     * creates a manifold with 0 points, with it's points array
     * full of instantiated ManifoldPoints.
     */
    public Manifold() {
        points = new ManifoldPoint[Settings.maxManifoldPoints];
        for (int i = 0; i < Settings.maxManifoldPoints; i++) {
            points[i] = new ManifoldPoint();
        }
        localNormal = new Vec2();
        localPoint = new Vec2();
        pointCount = 0;
    }

    /**
     * Creates this manifold as a copy of the other
     * @param other
     */
    public Manifold(Manifold other) {
        points = new ManifoldPoint[Settings.maxManifoldPoints];
        localNormal = other.localNormal.clone();
        localPoint = other.localPoint.clone();
        pointCount = other.pointCount;
    	type = other.type;
        // djm: this is correct now
        for(int i=0; i < Settings.maxManifoldPoints; i++){
    		points[i] = new ManifoldPoint(other.points[i]);
    	}
    }
    
    // djm for object reusability
    /**
     * copies this manifold from the given one
     * @param cp manifold to copy from
     */
    public void set(Manifold cp){
    	for(int i=0; i<cp.pointCount; i++){
    		points[i].set(cp.points[i]);
    	}
    	
    	type = cp.type;
    	localNormal.set(cp.localNormal);
    	localPoint.set( cp.localPoint);
    	pointCount = cp.pointCount;
    }
}