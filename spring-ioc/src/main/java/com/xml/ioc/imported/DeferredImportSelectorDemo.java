package com.xml.ioc.imported;

import com.xml.ioc.configurationclass.A;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * DeferredImportSelector的实现
 */
public class DeferredImportSelectorDemo implements DeferredImportSelector {


	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{A.class.getName()};
	}

	/**
	 * 若重写了getImportGroup且返回的不为空则调用GroupDemo的process并返回selectImports
	 * 否则调用默认的DeferredImportSelectorGrouping并直接调用DeferredImportSelectorDemo的selectImports方法
	 * @return
	 */
	@Override
	public Class<? extends Group> getImportGroup() {
		return GroupDemo.class;
	}

	/**
	 * 必须是静态内部类
	 */
	private static class GroupDemo implements DeferredImportSelector.Group{

		List<Entry> entrys = new ArrayList<>();
		@Override
		public void process(AnnotationMetadata metadata, DeferredImportSelector selector) {
			String[] classNames = selector.selectImports(metadata);
			Stream.of(classNames).forEach(className->{
				entrys.add(new Entry(metadata,className));
			});
		}

		@Override
		public Iterable<Entry> selectImports() {
			return entrys;
		}
	}
}
