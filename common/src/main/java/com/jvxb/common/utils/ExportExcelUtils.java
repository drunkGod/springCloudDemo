package com.jvxb.common.utils;

import com.jvxb.common.exception.ServiceException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Excel导出的公共方法
 *@date 2016年12月2日 上午9:33:04
 */
public class ExportExcelUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ExportExcelUtils.class);

	public static void excprtStudentExcel(Map beans,String srcPath,OutputStream os){
		XLSTransformer transformer = new XLSTransformer();
		try {
			//获得模板的输入流
			InputStream in = new FileInputStream(srcPath);
			//将beans通过模板输入流写到workbook中
			Workbook workbook = transformer.transformXLS(in, beans);
			//将workbook中的内容用输出流写出去
			workbook.write(os);
		}catch (Exception e) {
			if(LOG.isDebugEnabled()){
				LOG.debug(e.getMessage());
			}
			throw new ServiceException("excel导出异常："+e.getMessage());
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					if(LOG.isDebugEnabled()){
						LOG.debug(e.getMessage());
					}
				}
			}
		}
	}

}
