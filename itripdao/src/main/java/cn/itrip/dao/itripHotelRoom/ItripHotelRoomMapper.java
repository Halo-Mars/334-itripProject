package cn.itrip.dao.itripHotelRoom;
import cn.itrip.pojo.ItripHotelRoom;
import cn.itrip.pojo.ItripHotelRoomVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripHotelRoomMapper {

	public  List<ItripHotelRoomVO> GetRoom(Map<String,Object> map);

	public ItripHotelRoom getItripHotelRoomById(@Param(value = "id") Long id)throws Exception;

	public List<ItripHotelRoom>	getItripHotelRoomListByMap(Map<String, Object> param)throws Exception;

	public Integer getItripHotelRoomCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItripHotelRoom(ItripHotelRoom itripHotelRoom)throws Exception;

	public Integer updateItripHotelRoom(ItripHotelRoom itripHotelRoom)throws Exception;

	public Integer deleteItripHotelRoomById(@Param(value = "id") Long id)throws Exception;

}
