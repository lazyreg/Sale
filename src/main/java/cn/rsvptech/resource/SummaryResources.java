package cn.rsvptech.resource;


import cn.rsvptech.server.OrderServer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Dai.Liangzhi (dlz@cn.rsvptech.cn)
 * @since 2018/8/21
 */
@Path("/Summary")
public class SummaryResources {
  @GET
  @Produces({ MediaType.TEXT_PLAIN })
  public Response resolve() {
    String response = OrderServer.getInstance().getOrderStatistics();

    return Response.ok(response, MediaType.APPLICATION_JSON + "; charset=utf-8").build();
  }
}
