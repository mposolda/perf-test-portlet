package org.gatein.example.portlet;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfileHandler;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Test for simulating operations, which are most frequent during performance test of logged-user
 *
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class PerfTestPortlet extends GenericPortlet
{
   private UserProfileHandler userProfileHandler;
   private GroupHandler groupHandler;

   @Override
   public void init() throws PortletException
   {
      ExoContainer container = ExoContainerContext.getCurrentContainer();
      OrganizationService orgService = (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class);
      userProfileHandler = orgService.getUserProfileHandler();
      groupHandler = orgService.getGroupHandler();
   }

   @Override
   protected void doView(RenderRequest req, RenderResponse resp) throws PortletException
   {
      try
      {
         for (int j=1 ; j<50 ; j++)
         {
            String currentUser = "single" + j;
            System.out.println("Starting with user " + currentUser);
            for (int i=0 ; i<1000; i++)
            {
               userProfileHandler.findUserProfileByName(currentUser);
               groupHandler.findGroupsOfUser(currentUser);
               groupHandler.findGroupById("/platform/users");
            }
         }
      }
      catch (Exception e)
      {
         throw new PortletException(e);
      }
   }
}
