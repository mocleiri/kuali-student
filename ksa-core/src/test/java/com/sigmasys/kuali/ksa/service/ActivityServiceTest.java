package com.sigmasys.kuali.ksa.service;


import com.sigmasys.kuali.ksa.annotation.UseWebContext;
import com.sigmasys.kuali.ksa.model.Activity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@UseWebContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional
public class ActivityServiceTest extends AbstractServiceTest {

    @Autowired
    private ActivityService activityService;


    @Test
    public void getActivity() throws Exception {

        Activity activity = activityService.getActivity(1L);

        Assert.notNull(activity);
        Assert.notNull(activity.getId());

        Assert.isTrue(activity.getId().equals(1L));

    }

    @Test
    public void getActivities() throws Exception {

        List<Activity> activities = activityService.getActivities();

        Assert.notNull(activities);
        Assert.isTrue(!activities.isEmpty());

        for (Activity activity : activities) {
            Assert.notNull(activity);
            Assert.notNull(activity.getId());
        }

    }

    @Test
    public void getActivitiesForUser() throws Exception {

        List<Activity> activities = activityService.getActivities("admin");

        Assert.notNull(activities);
        Assert.isTrue(activities.isEmpty());

    }


    @Test
    public void updateActivity() throws Exception {

        Activity activity = activityService.getActivity(1L);

        Assert.notNull(activity);
        Assert.notNull(activity.getId());

        Assert.isTrue(activity.getId().equals(1L));

        activity.setAttribute("Attribute_1");


        activityService.persistActivity(activity);

        activity = activityService.getActivity(1L);

        Assert.notNull(activity);
        Assert.notNull(activity.getAttribute());
        Assert.isTrue(activity.getAttribute().equals("Attribute_1"));

    }


}
