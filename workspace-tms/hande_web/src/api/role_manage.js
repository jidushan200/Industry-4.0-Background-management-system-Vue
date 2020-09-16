import axios from "@/libs/api.request";
export const getRolePageList = data => {
  return axios.request({
    url: "/role/sys-role-page-list.json",
    data: qs.stringify(data),
    method: "post"
  });
};

export const removeRoleById = data => {
  return axios.request({
    url: "/role/sys-role-delete.json",
    data: qs.stringify(data),
    method: "post"
  });
};

export const removeRoleByIds = data => {
  return axios.request({
    url: "/role/sys-role-batch-delete.json",
    data: qs.stringify(data),
    method: "post"
  });
};

export const addRole = data => {
  return axios.request({
    url: "/role/sys-role-add.json",
    data: qs.stringify(data),
    method: "post"
  });
};

export const updateRole = data => {
  return axios.request({
    url: "/role/sys-role-update.json",
    data: qs.stringify(data),
    method: "post"
  });
};

export const authList = data => {
  return axios.request({
    url: "/auth/sys-auth-list.json",
    data: data,
    method: "post"
  });
};

export const roleAuthList = data => {
  return axios.request({
    url: "/auth/role-auth-get-by-role-id.json",
    data:qs.stringify(data),
    method: "post"
  });
};

export const roleAuthAdd = data => {
  return axios.request({
    url: "/auth/role-auth-save.json",
    data: data,
    method: "post"
  });
};
