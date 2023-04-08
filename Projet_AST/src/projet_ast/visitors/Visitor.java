package projet_ast.visitors;

import java.lang.*;
import java.util.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class Visitor extends ASTVisitor {
	@Override
	public boolean visit(MethodDeclaration node) {
		System.out.println("Methode : " + node.getName());
		return true;
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		try {
			Expression exp = node.getExpression();
			System.out.println("Methode appelee : " + node.getName().getIdentifier());
			System.out.println("La classe appartenue : " + exp.resolveTypeBinding().getQualifiedName());
			return true;}
		catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		System.out.println("Class : " + node.getName());
		return true;
	}
}
