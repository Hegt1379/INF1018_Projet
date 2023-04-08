package projet_ast.handlers;

import java.io.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

import java.util.*;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import projet_ast.visitors.Visitor;

public class SampleHandler extends AbstractHandler{
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException{
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "AST_HelloWorld", "Partie de AST");

		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		// Loop over all projects
		
		
		for (IProject project : projects) {
			try {
				if (!project.isOpen())
					continue;
					
				exploreProject(project);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private void exploreProject(IProject project) throws CoreException{
				if (!project.isOpen()) // Analyse only open projects
					return;
				System.out.println("Working in project " + project.getName());
				// check if we have a Java project
				if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
					IJavaProject javaProject = JavaCore.create(project);
					explorePackage(javaProject);

					System.out.println("Project (" + project.getName() + ") done");
				}
	}

	private void explorePackage(IJavaProject javaProject) throws JavaModelException {

		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			// Package fragments include all packages in the
			// classpath
			// We will only look at the packages from the source
			// folder
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				System.out.println("_________________________________________");
				System.out.println("Package " + mypackage.getElementName());
				exploreCompiplationUnit(mypackage);

			}

		}
	}

	private void exploreCompiplationUnit(IPackageFragment mypackage) throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			System.out.println("_________________________________________");
			//System.out.println("Package " + mypackage.getElementName());
			//System.out.println("CompUnit " + unit.getElementName());

			CompilationUnit parse = parseUnit(unit);
			Visitor visitor = new Visitor();
			
			try {
				parse.accept(visitor);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//method_cnt(parse);
		}
	}

	private CompilationUnit parseUnit(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS12);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null);
	}
	
	/*private void method_cnt(CompilationUnit cu) {
		List<AbstractTypeDeclaration> types = cu.types();
		List<Expression> exps = cu.types();
		for(AbstractTypeDeclaration type:types) {
			if(type.getNodeType() == ASTNode.TYPE_DECLARATION) {
				List<BodyDeclaration> bodies = type.bodyDeclarations();
				
				for(BodyDeclaration body: bodies) {
					if(body.getNodeType() == ASTNode.METHOD_DECLARATION) {
						MethodDeclaration method = (MethodDeclaration)body;
						System.out.println("Methode :" + method.getName());
					}
				}
			}
			
		}
			for(AbstractTypeDeclaration type:types) {
				if(type.getNodeType() == ASTNode.TYPE_DECLARATION) {
					List<BodyDeclaration> bodies = type.bodyDeclarations();
					for(BodyDeclaration body: bodies) {
						if(body.getNodeType() == ASTNode.METHOD_DECLARATION) {
							MethodDeclaration method = (MethodDeclaration)body;
							System.out.println("Methode :" + method.getName());
						}
					}
				}
				
			}
	}*/
		
		
		//System.out.println("Methode : " + node.getName());
		//if(node.getBody()!= null) {
			//node.
			//for(Object state : node.getBody().statements()) {
				//System.out.println("state: " + state);
				//state.
			//}
		//}
		//node.getBody().statements().contains(MethodInvocation)
		//System.out.println("sttm: " +node.getBody().statements());
	//}
}

