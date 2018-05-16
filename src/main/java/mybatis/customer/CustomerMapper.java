package mybatis.customer;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 继承自己的MyMapper
 */
public interface CustomerMapper<T> extends Mapper<T>, MySqlMapper<T> {
	
}
