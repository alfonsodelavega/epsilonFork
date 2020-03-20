package org.eclipse.epsilon.picto.source;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.common.dt.console.EpsilonConsole;
import org.eclipse.epsilon.common.dt.launching.extensions.ModelTypeExtension;
import org.eclipse.epsilon.common.dt.util.LogUtil;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.common.util.UriUtil;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.egl.IEgxModule;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dt.ExtensionPointToolNativeTypeDelegate;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.picto.Layer;
import org.eclipse.epsilon.picto.LazyEgxModule;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.ResourceLoadingException;
import org.eclipse.epsilon.picto.StringContentPromise;
import org.eclipse.epsilon.picto.ViewTree;
import org.eclipse.epsilon.picto.dom.Model;
import org.eclipse.epsilon.picto.dom.Parameter;
import org.eclipse.epsilon.picto.dom.Picto;
import org.eclipse.ui.IEditorPart;

public abstract class EglPictoSource implements PictoSource {
	
	protected List<IModel> models = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public ViewTree getViewTree(IEditorPart editor) throws Exception {
		
		IFile iFile = waitForFile(editor);
		if (iFile == null) return createEmptyViewTree();
		
		File modelFile = new File(iFile.getLocation().toOSString());
		Resource resource;
		ViewTree viewTree = new ViewTree();
		
		try {
			resource = getResource(editor);
		}
		catch (Exception ex) {
			throw new ResourceLoadingException(ex);
		}
		
		Picto renderingMetadata = getRenderingMetadata(editor);
		
		if (renderingMetadata != null) {
			IEolModule module;	
			IModel model = null;
			
			//if (renderingMetadata.getNsuri() != null) {
			//	EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(renderingMetadata.getNsuri());
			//	model = new InMemoryEmfModel("M", resource, ePackage);
			//}
			//else {
			if (resource != null) {
				model = new InMemoryEmfModel("M", resource);
				((InMemoryEmfModel) model).setExpand(false);
			}
			//}
			
			if (renderingMetadata.getFormat().equals("egx")) {
				module = new LazyEgxModule();
			}
			else {
				module = new EglTemplateFactoryModuleAdapter(new EglFileGeneratingTemplateFactory());
			}
			
			module.getContext().getNativeTypeDelegates().add(new ExtensionPointToolNativeTypeDelegate());
			
			if (renderingMetadata.getTemplate() == null) throw new Exception("No EGL file specified.");
						
			URI templateUri = UriUtil.resolve(renderingMetadata.getTemplate(), modelFile.toURI());
			module.parse(templateUri);
			
			IEolContext context = module.getContext();
			context.setOutputStream(EpsilonConsole.getInstance().getDebugStream());
			context.setErrorStream(EpsilonConsole.getInstance().getErrorStream());
			context.setWarningStream(EpsilonConsole.getInstance().getWarningStream());		
			if (model != null) context.getModelRepository().addModel(model);
			
			for (Model pictoModel : renderingMetadata.getModels()) {
				model = loadModel(pictoModel, modelFile);
				if (model != null) models.add(model);
			}
			
			context.getModelRepository().addModels(models);
			
			if (renderingMetadata.getFormat().equals("egx")) {
								
				List<LazyGenerationRuleContentPromise> instances = (List<LazyGenerationRuleContentPromise>) module.execute();
				for (LazyGenerationRuleContentPromise instance : instances) {
					String format = "html";
					String icon = "cccccc";
					Collection<String> path = new ArrayList<>(Arrays.asList(""));
					List<Layer> layers = new ArrayList<>();
					Variable layersVariable = null;
					
					for (Variable variable : instance.getVariables()) {
						switch (variable.getName()) {
						case "format": format = variable.getValue() + ""; break;
						case "path": path = (Collection<String>) variable.getValue(); break;
						case "icon": icon = variable.getValue() + ""; break;
						case "layers": {
								layersVariable = variable;
								List<Object> layerMaps = (List<Object>) variable.getValue();
								for (Object layerMapObject : layerMaps) {
									Map<Object, Object> layerMap = (Map<Object, Object>) layerMapObject;
									Layer layer = new Layer();
									layer.setId(layerMap.get("id") + "");
									layer.setTitle(layerMap.get("title") + "");
									if (layerMap.containsKey("active")) layer.setActive((Boolean) layerMap.get("active"));
									layers.add(layer);
								}
							}
						}
					}
					
					// Replace layers variable from list of maps to list of Layer objects
					if (layersVariable != null) instance.getVariables().remove(layersVariable);
					instance.getVariables().add(Variable.createReadOnlyVariable("layers", layers));
					viewTree.addPath(new ArrayList<>(path), instance, format, icon, layers);
				}
				
			}
			else {
				String content = module.execute() + "";
				viewTree = new ViewTree();
				viewTree.setPromise(new StringContentPromise(content));
				viewTree.setFormat(renderingMetadata.getFormat());
			}
			
			return viewTree;
		}
		else {
			return createEmptyViewTree();
		}
		
	}
	
	protected IFile waitForFile(IEditorPart editorPart) {
		int attempts = 0;
		int maxAttempts = 50;
		IFile file = getFile(editorPart);
		while (file == null && attempts < maxAttempts) {
			try { Thread.sleep(100); } catch (InterruptedException e) {}
			file = getFile(editorPart);
			attempts++;
		}
		return file;
	}
	
	protected ViewTree createEmptyViewTree() {
		ViewTree viewTree = new ViewTree();
		viewTree.setPromise(new StringContentPromise("Nothing to render"));
		viewTree.setFormat("text");
		return viewTree;
	}
	
	public void dispose() {
		for (IModel model : models) {
			try {
				model.dispose();
			}
			catch (Exception ex) {
				LogUtil.log(ex);
			}
		}
		models.clear();
	}
	
	protected IModel loadModel(Model model, File baseFile) throws Exception {
		IModel m = ModelTypeExtension.forType(model.getType()).createModel();
		m.setName(model.getName());
		m.setReadOnLoad(true);
		m.setStoredOnDisposal(false);
		StringProperties properties = new StringProperties();
		IRelativePathResolver relativePathResolver = new IRelativePathResolver() {
			
			@Override
			public String resolve(String relativePath) {
				return new File(baseFile.getParentFile(), relativePath).getAbsolutePath();
			}
		};
		
		for (Parameter parameter : model.getParameters()) {
			if (parameter.getFile() != null) {
				properties.put(parameter.getName(), relativePathResolver.resolve(parameter.getFile()));
			}
			else {
				properties.put(parameter.getName(), parameter.getValue());
			}
		}
		m.load(properties, relativePathResolver);
		return m;
	}
	
	@Override
	public boolean supports(IEditorPart editorPart) {
		return supportsEditorType(editorPart) && getRenderingMetadata(editorPart) != null;
	}
	
	protected abstract Picto getRenderingMetadata(IEditorPart editorPart);
	
	protected abstract Resource getResource(IEditorPart editorPart);
	
	protected abstract IFile getFile(IEditorPart editorPart);
	
	protected abstract boolean supportsEditorType(IEditorPart editorPart);
}
