package org.flowable.testing.glue;

import java.util.List;

import org.flowable.engine.IdentityService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;

import io.cucumber.java.en.Given;
import org.flowable.testing.service.FlowableServices;

public class IdentitySteps {

    private final IdentityService identityService;

    public IdentitySteps(FlowableServices flowableServices) {
        this.identityService = flowableServices.getIdentityService();
    }


    @Given("a user with the id {string} exists")
    public void aUserWithTheIdExists(String userId) {
        User user = identityService.newUser(userId);
        identityService.saveUser(user);
    }

    @Given("a group with the id {string} exists")
    public void aGroupWithTheIdExists(String groupId) {
        Group group = identityService.newGroup(groupId);
        identityService.saveGroup(group);
    }

    @Given("the user {string} is a member of the group {string}")
    public void aUserBelongsToTheGroup(String userId, String groupId) {
        identityService.createMembership(userId, groupId);
    }

    @Given("the following users exist:")
    public void theFollowingUsersExist(List<String> userIds) {
        for (String userId : userIds) {
            aUserWithTheIdExists(userId);
        }
    }

    @Given("the following groups exist:")
    public void theFollowingGroupsExist(List<String> groupIds) {
        for (String groupId : groupIds) {
            aGroupWithTheIdExists(groupId);
        }
    }

    @Given("the user {string} is a member of the following groups:")
    public void isAMemberOfTheFollowingGroups(String userId, List<String> groupIds) {
        for (String groupId : groupIds) {
            aUserBelongsToTheGroup(userId, groupId);
        }
    }
}
