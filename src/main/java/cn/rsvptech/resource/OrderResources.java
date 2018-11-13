package cn.rsvptech.resource;

import cn.rsvptech.server.OrderServer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Dai.Liangzhi (dlz@cn.rsvptech.cn)
 * @since 2018/8/21
 */
@Path("/buy")
public class OrderResources {
  @GET
  @Produces({ MediaType.TEXT_PLAIN })
  public Response resolve(@QueryParam("userid") String userid,

          @QueryParam("goodid") String goodid,

          @QueryParam("number") String number) {
    int numbers = Integer.parseInt(number);
    String response = OrderServer.getInstance().processOrder(userid, goodid, numbers);
    return Response.ok(response, MediaType.APPLICATION_JSON + "; charset=utf-8").build();
  }
}
