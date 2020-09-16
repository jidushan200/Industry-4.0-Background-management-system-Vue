import axios from "@/libs/api.request";
import qs from "qs";
export const getUserPageList = data => { 
  return axios.request({
    url: "/user/user-account-page-list.json",
    params: data,
    isAuth:true,
    method: "post"
  })
};

export const removeUserById = data => { 
  return axios.request({
    url: "/user/user-account-delete.json",
    params: data,
    isAuth:true,
    method: "post"
  })
};

export const removeUserByIds = data => {
  return axios.request({
    url: "/user/user-account-batch-delete.json",
    data: data,
    isAuth:true,
    method: "post"
  })
};




export const getRoleList = data => {
  return axios.request({
    url: "/role/sys-role-list.json",
    params: data,
    isAuth:true,
    method: "post"
  })
};

export const resetPasswordById = data => { 
  return axios.request({
    url: "/user/user-loginpwd-update.json",
    params: data,
    isAuth:true,
    method: "post"
  })
};