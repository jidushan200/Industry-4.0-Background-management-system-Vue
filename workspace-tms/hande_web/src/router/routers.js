import Main from '@/components/main'
import parentView from '@/components/parent-view'
import { access } from 'fs'

/**
 * iview-admin中meta除了原生参数外可配置的参数: meta: { title: { String|Number|Function }
 * 显示在侧边栏、面包屑和标签栏的文字 使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 * 可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由 hideInBread: (false)
 * 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置 hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 * notCache: (false)
 * 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致 access:
 * (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由 icon: (-)
 * 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_' beforeCloseName: (-)
 * 设置该字段，则在关闭当前tab页时会去'@/router/before-close.js'里寻找该字段名对应的方法，作为关闭前的钩子函数 }
 */

export default [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: 'Login - 登录',
      hideInMenu: true
    },
    component: () => import('@/view/login/login.vue')
  },
  {
    path: '/',
    name: '_home',
    redirect: '/home',
    component: Main,
    meta: {
      hideInMenu: true,
      notCache: true
    },
    children: [
      {
        path: '/home',
        name: 'home',
        meta: {
          hideInMenu: true,
          title: '首页',
          notCache: true,
          icon: 'md-home'
        },
        component: () => import('@/view/single-page/home')
      }
    ]
  },
  {
    path: '/message',
    name: 'message',
    component: Main,
    meta: {
      hideInBread: true,
      hideInMenu: true
    },
    children: [
      {
        path: 'message-page',
        name: 'message_page',
        meta: {
          icon: 'md-notifications',
          title: '消息中心'
        },
        component: () => import('@/view/single-page/message/index.vue')
      }
    ]
  },

  {
    path: '/sys-manage',
    name: 'sys_manage',
    meta: {
      icon: 'md-cloud-upload',
      title: '系统管理',
      access: ['0101']
    },
    component: Main,
    children: [
      {
        path: 'role_manage',
        name: 'role_manage',
        meta: {
          icon: 'md-clipboard',
          title: '角色管理',
          access: ['010101']
        },
        component: () => import('@/view/sys-manage/role-manage.vue')
      },
      {
        path: 'user_manage',
        name: 'user_manage',
        meta: {
          icon: 'ios-contacts',
          title: '用户管理',
          access: ['010102']
        },
        component: () => import('@/view/sys-manage/user-manage.vue')
      },
      {
        path: 'log_manage',
        name: 'log_manage',
        meta: {
          icon: 'ios-clipboard',
          title: '日志管理',
          access: ['010103']
        },
        component: () => import('@/view/sys-manage/operation-log-manage.vue')
      },
      {
        path: 'sys_param',
        name: 'sys_param',
        meta: {
          icon: 'md-barcode',
          title: '系统参数管理',
          access: ['010104']
        },
        component: () =>
          import('@/view/sys-manage/sys-param/sys-param-manage.vue')
      }
    ]
  },
  {
    path: '/components',
    name: 'base_data_manage',
    meta: {
      icon: 'logo-buffer',
      title: '基础数据管理',
      access: ['0102']
    },
    component: Main,
    children: [
      {
        path: 'staff-department-page',
        name: 'staff_department_manage',
        meta: {
          icon: 'ios-chatbubbles',
          title: '人员部门管理',
          access: ['010201']
        },
        component: () =>
          import(
            '@/view/base-manage/staffDepartment/staff-department-manage.vue'
          )
      },
      {
        path: 'team-page',
        name: 'team_manage',
        meta: {
          icon: 'ios-list-box',
          title: '班组管理',
          access: ['010202']
        },
        component: () => import('@/view/base-manage/team/team-manage.vue')
      },
      {
        path: 'staff-page',
        name: 'staff_manage',
        meta: {
          icon: 'ios-people',
          title: '人员管理',
          access: ['010203']
        },
        component: () => import('@/view/base-manage/staff/staff-manage.vue')
      },
      {
        path: 'supplier-select-page',
        name: 'supplier_manage',
        meta: {
          icon: 'ios-star',
          title: '供应商管理',
          access: ['010204']
        },
        component: () =>
          import('@/view/base-manage/supplier/supplier-manage.vue')
      },
      {
        path: 'equipment-page',
        name: 'equipment_manage',
        meta: {
          icon: 'ios-nuclear',
          title: '设备管理',
          access: ['010205']
        },
        component: () =>
          import('@/view/base-manage/equipment/equipment-manage.vue')
      },
      {
        path: 'terminal-page',
        name: 'terminal_manage',
        meta: {
          icon: 'md-desktop',
          title: '终端管理',
          access: ['010206']
        },
        component: () =>
          import('@/view/base-manage/terminal/terminal-manage.vue')
      },
      {
        path: 'part-page',
        name: 'part_manage',
        meta: {
          icon: 'md-cog',
          title: '制件管理',
          access: ['010207']
        },
        component: () => import('@/view/base-manage/part/part-manage.vue')
      },
      {
        path: 'check-standard-page',
        name: 'check_standard_manage',
        meta: {
          icon: 'ios-create',
          title: '质检标准管理',
          access: ['010208']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'tool-check-standard',
            name: 'tool_check_standard',
            meta: {
              // icon: "ios-create",
              title: '刀具质检标准',
              access: ['010208']
            },
            component: () =>
              import(
                '@/view/base-manage/check-standard/tool-check-standard.vue'
              )
          },
          {
            path: 'head-check-standard',
            name: 'head_check_standard',
            meta: {
              title: '组合质检标准',
              access: ['010208']
            },
            component: () =>
              import(
                '@/view/base-manage/check-standard/head-check-standard.vue'
              )
          },
          {
            path: 'fixture-check-standard',
            name: 'fixture_check_standard',
            meta: {
              // icon: "ios-nuclear-outline",
              title: '夹具质检标准',
              access: ['010208']
            },
            component: () =>
              import(
                '@/view/base-manage/check-standard/fixture-check-standard.vue'
              )
          }
        ]
      },
      {
        path: '/tool-base-page',
        name: 'tool_base_page',
        meta: {
          icon: 'ios-planet',
          title: '刀具基础信息管理',
          access: ['010209']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'tool-base-manage',
            name: 'tool_base_manage',
            meta: {
              // icon: "ios-create",
              title: '刀具基础信息',
              access: ['01020901']
            },
            component: () =>
              import('@/view/tool-manage/tool-base/tool-base-manage.vue')
          },
          {
            path: 'tool-part-page',
            name: 'tool_part_manage',
            meta: {
              // icon: "ios-nuclear-outline",
              title: '物料制件管理',
              access: ['01020902']
            },
            component: () =>
              import('@/view/tool-manage/tool-part/tool-part-manage.vue')
          },
          {
            path: 'coat-price-page',
            name: 'coat_price_manage',
            meta: {
              // icon: "logo-yen",
              title: '涂层价格管理',
              access: ['01020903']
            },
            component: () =>
              import('@/view/tool-manage/coat-price/coat-price-manage.vue')
          },
          {
            path: 'repair-price-page',
            name: 'repair_price_manage',
            meta: {
              // icon: "logo-yen",
              title: '刃磨价格管理',
              access: ['01020904']
            },
            component: () =>
              import('@/view/tool-manage/repair-price/repair-price-manage.vue')
          },
          {
            path: 'head-manage',
            name: 'head_manage',
            meta: {
              // icon: "ios-aperture-outline",
              title: '刀条组合',
              access: ['01020905']
            },
            component: () =>
              import('@/view/tool-manage/tool-base/tool-head-mange.vue')
          },
          {
            path: 'head-blade-manage',
            name: 'head_blade_manage',
            meta: {
              // icon: "ios-aperture-outline",
              title: '刀条组合管理',
              access: ['01020905']
            },
            component: () =>
              import('@/view/tool-manage/tool-base/head-blade-mange.vue')
          },
          {
            path: 'head-part-manage',
            name: 'head_part_manage',
            meta: {
              // icon: "ios-pricetags-outline",
              title: '刀条组合制件',
              access: ['01020906']
            },
            component: () =>
              import('@/view/tool-manage/tool-base/head-part-mange.vue')
          }
        ]
      },
      {
        path: '/measure-base-page',
        name: 'measure_base_page',
        meta: {
          icon: 'ios-clipboard',
          title: '量具基础信息管理',
          access: ['010210']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: '/measure-base-manage',
            name: 'measure_base_manage',
            meta: {
              // icon: "ios-clipboard",
              title: '量具基础信息',
              access: ['010210']
            },
            component: () =>
              import(
                '@/view/measure-manage/measure-base/measure-base-manage.vue'
              )
          }
        ]
      },
      {
        path: '/fixture-base-page',
        name: 'fixture_base_page',
        meta: {
          icon: 'ios-map',
          title: '夹具基础信息管理',
          access: ['010211']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'fixture-base-manage',
            name: 'fixture_base_manage',
            meta: {
              // icon: "ios-map",
              title: '夹具基础信息管理',
              access: ['01021101']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-base/fixture-base-manage.vue'
              )
          },
          {
            path: 'fixture-part-page',
            name: 'fixture_part_manage',
            meta: {
              //  icon: "md-pricetags",
              title: '夹具制件管理',
              access: ['01021102']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-part/fixture-part-manage.vue'
              )
          }
        ]
      },
      {
        path: '/mould-base-page',
        name: 'mould_base_page',
        meta: {
          icon: 'ios-barcode',
          title: ' 模具基础信息管理',
          access: ['010212']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'mould-base-manage',
            name: 'mould_base_manage',
            meta: {
              // icon: "ios-barcode",
              title: ' 模具基础信息管理',
              access: ['01021202']
            },
            component: () =>
              import('@/view/mould-manage/mould-base/mould-base-manage.vue')
          },
          {
            path: 'mould-part-page',
            name: 'mould_part_manage',
            meta: {
              // icon: "md-pricetags",
              title: '模具制件管理',
              access: ['01021202']
            },
            component: () =>
              import('@/view/mould-manage/mould-part/mould-part-manage.vue')
          },
          {
            path: 'mould-repair-procedure',
            name: 'mould_repair_procedure',
            meta: {
              //  icon: "md-pricetags",
              title: '模具修磨工序',
              access: ['01021203']
            },
            component: () =>
              import(
                '@/view/mould-manage/mould-repair-procedure/repair-procedure-manage.vue'
              )
          },
          {
            path: 'mould-embryo-manage',
            name: 'mould_embryo_manage',
            meta: {
              //  icon: "md-pricetags",
              title: '模具坯管理',
              access: ['01021204']
            },
            component: () =>
              import('@/view/mould-manage/mould-embryo/mould-embryo-manage.vue')
          }
        ]
      }
    ]
  },

  {
    path: '/tool-manage',
    name: 'tool_manage',
    meta: {
      icon: 'logo-codepen',
      title: '刀具管理',
      access: ['0103']
    },
    component: Main,
    children: [
      {
        path: 'tool-purchase-list',
        name: 'tool_purchase_list',
        meta: {
          // icon: "ios-cart",
          title: '刀具申购',
          access: ['010301']
        },
        component: () =>
          import(
            '@/view/tool-manage/tool-purchase-manage/tool-purchase-list/purchase-tabs.vue'
          )
      },
      {
        path: 'tool-purchase',
        name: 'tool_purchase',
        meta: {
          // icon: "ios-planet",
          title: '刀具采购',
          access: ['010302']
        },
        component: () =>
          import(
            '@/view/tool-manage/tool-purchase-manage/tool-purchase-manage/purchase-tabs.vue'
          )
      },
      {
        path: '/tool-stock-manage',
        name: 'tool_stock_manage',
        meta: {
          icon: 'md-medal',
          title: '刀具库管理',
          access: ['010303']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        // component: Main,
        children: [
          {
            path: 'tool-purchase-receive',
            name: 'tool_purchase_receive',
            meta: {
              // icon: "ios-plane",
              title: '采购收货',
              access: ['01030301']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-purchase-manage/tool-purchase-receive/tool-purchase.vue'
              )
          },
          {
            path: 'tool-wait-warehouse-list',
            name: 'tool_wait_warehouse_list',
            meta: {
              // icon: "md-list-box",
              title: '待入库刀具',
              access: ['01030302']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-list-manage/wait-warehouse-list.vue'
              )
          },
          {
            path: 'tool-lists-manage',
            name: 'tool_lists_manage',
            meta: {
              // icon: "md-list-box",
              title: '出入库管理',
              access: ['01030302']
            },
            component: () =>
              import('@/view/tool-manage/tool-list-manage/knife-lists.vue')
          },
          {
            path: 'tool-process-manage',
            name: 'tool_process_page',
            meta: {
              // icon: "md-calculator",
              title: '刀具交回',
              access: ['01030303']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-process-manage/tool-process-manage.vue'
              )
          },
          {
            path: 'wait-coat-blade',
            name: 'wait_coat_blade',
            meta: {
              // icon: "md-calculator",
              title: '待涂层刀条',
              access: ['01030304']
            },
            component: () =>
              import('@/view/tool-manage/tool-list-manage/wait-coat-blade.vue')
          },
          {
            path: 'tool-coat-process',
            name: 'tool_coat_process',
            meta: {
              title: '涂层生产计数',
              access: ['01030305']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-list-manage/tool-coat-process.vue'
              )
          },
          {
            path: 'tool-coat-statistics',
            name: 'tool_coat_statistics',
            meta: {
              title: '刀具涂层结算',
              access: ['01030306']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-list-manage/tool-coat-statistics.vue'
              )
          },
          {
            path: 'tool-scrap-manage',
            name: 'tool_scrap_page',
            meta: {
              title: '已报废刀具列表',
              access: ['01030307']
            },
            component: () =>
              import('@/view/tool-manage/tool-scrap-manage/scrap-tabs.vue')
          }
        ]
      },
      {
        path: '/tool-head-manage',
        name: 'tool_head_manage',
        meta: {
          icon: 'ios-copy',
          title: '螺伞刀具管理',
          access: ['010304']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'tool-head-list',
            name: 'tool_head_list',
            meta: {
              // icon: "md-list-box",
              title: '刀头管理',
              access: ['01030401']
            },
            component: () =>
              import('@/view/tool-manage/tool-head/tool-head-list.vue')
          },
          {
            path: 'wait-warehouse-list',
            name: 'blade_wait_warehouse_list',
            meta: {
              // icon: "md-list-box",
              title: '待入库刀条',
              access: ['01030402']
            },
            component: () =>
              import('@/view/tool-manage/tool-blade/wait-warehouse-list.vue')
          },
          {
            path: 'tool-plate-list',
            name: 'tool_plate_list',
            meta: {
              // icon: "md-list-box",
              title: '刀盘管理'
              // access: ["01030403"]
            },
            component: () =>
              import('@/view/tool-manage/tool-blade/tool-plate-list.vue')
          },
          {
            path: 'tool-blade-list',
            name: 'tool_blade_list',
            meta: {
              // icon: "md-list-box",
              title: '刀条管理',
              access: ['01030403']
            },
            component: () =>
              import('@/view/tool-manage/tool-blade/tool-blade-list.vue')
          },
          {
            path: 'head-blade-list',
            name: 'head_blade_list',
            meta: {
              // icon: "md-list-box",
              title: '刀条生产管理',
              access: ['01030404']
            },
            component: () =>
              import('@/view/tool-manage/tool-blade/head-blade-list.vue')
          },
          {
            path: 'blade-life-list',
            name: 'blade_life_list',
            meta: {
              // icon: "md-list-box",
              title: '刀条生命周期',
              access: ['01030405']
            },
            component: () =>
              import('@/view/tool-manage/tool-blade/blade-life-list.vue')
          },
          {
            path: 'blade-scrap-list',
            name: 'blade_scrap_list',
            meta: {
              // icon: "md-list-box",
              title: '刀条报废管理',
              access: ['01030406']
            },
            component: () =>
              import('@/view/tool-manage/tool-blade/blade-scrap-tab.vue')
          }
        ]
      },
      {
        path: '/tool-repair',
        name: 'tool_repair',
        meta: {
          icon: 'ios-construct',
          title: '刀具维护管理',
          access: ['010305']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'tool-repair-manage',
            name: 'tool_repair_manage',
            meta: {
              // icon: "md-build",
              title: '刀具待磨列表',
              access: ['01030501']
            },
            component: () =>
              import('@/view/tool-manage/tool-repair-manage/repair-tabs.vue')
          },
          {
            path: 'tool-head-repair-manage',
            name: 'tool_head_repair_manage',
            meta: {
              // icon: "md-build",
              title: '刀头待磨列表',
              access: ['01030502']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-repair-manage/head-repair-tabs.vue'
              )
          },
          {
            path: 'blade-repair-manage',
            name: 'blade_repair_manage',
            meta: {
              // icon: "md-build",
              title: '刀条待磨列表',
              access: ['01030503']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-repair-manage/blade-repair-tabs.vue'
              )
          },
          {
            path: 'tool-coat-manage',
            name: 'tool_coat_manage',
            meta: {
              // icon: "md-color-wand",
              title: '刀具送涂列表',
              access: ['01030504']
            },
            component: () =>
              import('@/view/tool-manage/tool-coat-manage/tool-coat-list.vue')
          }
        ]
      },
      {
        path: '/tool-check-manage',
        name: 'tool_check_manage',
        meta: {
          icon: 'ios-paper',
          title: '刀具质检管理',
          access: ['010306']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'new-tool-check',
            name: 'new_tool_check',
            meta: {
              // icon: "ios-appstore-outline",
              title: '新刀质检',
              access: ['01030601']
            },
            component: () =>
              import('@/view/tool-manage/tool-check-manage/new-check-tab.vue')
          },
          {
            path: 'repair-tool-check',
            name: 'repair_tool_check',
            meta: {
              // icon: "ios-appstore-outline",
              title: '刀具刃磨质检',
              access: ['01030602']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-check-manage/repair-check-tab.vue'
              )
          },
          {
            path: 'coat-tool-check',
            name: 'coat_tool_check',
            meta: {
              // icon: "ios-appstore-outline",
              title: '刀具涂层质检',
              access: ['01030604']
            },
            component: () =>
              import('@/view/tool-manage/tool-check-manage/coat-check-tab.vue')
          },
          {
            path: 'repair-compose-check',
            name: 'repair_compose_check',
            meta: {
              // icon: "ios-appstore-outline",
              title: '组合刃磨质检',
              access: ['01030602']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-check-manage/repair-compose-check-tab.vue'
              )
          },

          {
            path: 'coat-compose-check',
            name: 'coat_compose_check',
            meta: {
              // icon: "ios-appstore-outline",
              title: '组合涂层质检',
              access: ['01030604']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-check-manage/coat-compose-check-tab.vue'
              )
          },
          {
            path: '/tool-unqualified-manage',
            name: 'tool_unqualified_manage',
            meta: {
              icon: 'ios-remove-circle',
              title: '不合格管理',
              access: ['01030603']
            },
            component: () =>
              import(
                '@/view/article-publish-center/article-publish-center.vue'
              ),
            children: [
              {
                path: 'new-unqualified-manage',
                name: 'new_unqualified_manage',
                meta: {
                  // icon: "ios-hand",
                  title: '新刀不合格列表',
                  access: ['01030603']
                },
                component: () =>
                  import(
                    '@/view/tool-manage/tool-unqualified-manage/new-unqualified-list.vue'
                  )
              },
              {
                path: 'repair-unqualified-manage',
                name: 'repair_unqualified_manage',
                meta: {
                  // icon: "md-construct",
                  title: '刃磨不合格列表',
                  access: ['01030603']
                },
                component: () =>
                  import(
                    '@/view/tool-manage/tool-unqualified-manage/repair-unqualified-list.vue'
                  )
              },
              {
                path: 'coat-unqualified-manage',
                name: 'coat_unqualified_manage',
                meta: {
                  // icon: "md-color-palette",
                  title: '涂层不合格列表',
                  access: ['01030603']
                },
                component: () =>
                  import(
                    '@/view/tool-manage/tool-unqualified-manage/coat-unqualified-list.vue'
                  )
              }
            ]
          }
        ]
      },
      {
        path: '/tool-use-manage',
        name: 'tool_use_manage',
        meta: {
          icon: 'ios-stats',
          title: '刀具统计',
          access: ['010307']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'tool-unusual-manage',
            name: 'tool_unusual_page',
            meta: {
              // icon: "ios-warning",
              title: '刀具异常报告列表',
              access: ['01030701']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-unusual-manage/tool-unusual-manage.vue'
              )
          },
          {
            path: 'tool-process-list',
            name: 'tool_process_list',
            meta: {
              title: '生产统计报表',
              access: ['01030702']
            },
            component: () =>
              import('@/view/tool-manage/tool-life-manage/process-list.vue')
          },
          {
            path: 'tool-life-manage',
            name: 'tool_life_page',
            meta: {
              title: '刀具完整生命周期',
              access: ['01030703']
            },
            component: () =>
              import('@/view/tool-manage/tool-life-manage/tool-life-manage.vue')
          },
          {
            path: 'tool-cost-performance',
            name: 'tool_cost_performance',
            meta: {
              title: '刀具性价比',
              access: ['01030704']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-list-manage/tool-cost-performance.vue'
              )
          },
          {
            path: 'coat-cost-performance',
            name: 'coat_cost_performance',
            meta: {
              title: '涂层性价比',
              access: ['01030705']
            },
            component: () =>
              import(
                '@/view/tool-manage/tool-list-manage/coat-cost-performance.vue'
              )
          }
        ]
      }
    ]
  },
  {
    path: '/measure-manage',
    name: 'measure_manage',
    meta: {
      icon: 'ios-appstore',
      title: '量具管理',
      access: ['0104']
    },
    component: Main,
    children: [
      {
        path: 'measure-purchase-list',
        name: 'measure_purchase_list',
        meta: {
          //  icon: "ios-cart",
          title: '量具申购列表',
          access: ['010401']
        },
        component: () =>
          import(
            '@/view/measure-manage/measure-purchase-manage/measure-purchase-list/purchase-tabs.vue'
          )
      },
      {
        path: 'measure-purchase',
        name: 'measure_purchase',
        meta: {
          // icon: "ios-planet",
          title: '量具采购',
          access: ['010402']
        },
        component: () =>
          import(
            '@/view/measure-manage/measure-purchase-manage/measure-purchase-manage/purchase-tabs.vue'
          )
      },
      {
        path: 'measure-produce',
        name: 'measure_produce',
        meta: {
          // icon: "ios-link",
          title: '量具自制',
          access: ['010403']
        },
        component: () =>
          import(
            '@/view/measure-manage/measure-produce-manage/measure-produce-receive.vue'
          )
      },
      {
        path: '/measure-stock-manage',
        name: 'measure_stock_manage',
        meta: {
          icon: 'md-medal',
          title: '量具库管理',
          access: ['010404']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'measure-purchase-receive',
            name: 'measure_purchase_receive',
            meta: {
              // icon: "ios-plane",
              title: '量具采购收货',
              access: ['01040401']
            },
            component: () =>
              import(
                '@/view/measure-manage/measure-purchase-manage/measure-purchase-receive/measure-purchase-receive.vue'
              )
          },
          {
            path: 'measure-lists-wait',
            name: 'measure_lists_wait',
            meta: {
              // icon: "md-list-box",
              title: '待入库列表',
              access: ['01040402']
            },
            component: () =>
              import(
                '@/view/measure-manage/measure-list-wait/measure-list-wait.vue'
              )
          },
          {
            path: 'measure-check-return',
            name: 'measure_check_return',
            meta: {
              title: '待退回列表',
              access: ['01040402']
            },
            component: () =>
              import(
                '@/view/measure-manage/measure-list-wait/return-check-list.vue'
              )
          },
          {
            path: 'measure-lists-manage',
            name: 'measure_lists_manage',
            meta: {
              // icon: "md-list-box",
              title: '台账管理',
              access: ['01040403']
            },
            component: () =>
              import(
                '@/view/measure-manage/measure-list-manage/measure-lists.vue'
              )
          },
          {
            path: 'measure-life-manage',
            name: 'measure_life_manage',
            meta: {
              title: '生命周期',
              access: ['01040404']
            },
            component: () =>
              import(
                '@/view/measure-manage/measure-life-manage/measure-life-manage.vue'
              )
          },
          {
            path: 'measure-scrap-manage',
            name: 'measure_scrap_page',
            meta: {
              title: '已报废量具列表',
              access: ['01040405']
            },
            component: () =>
              import(
                '@/view/measure-manage/measure-scrip-manage/measure-scrip-list.vue'
              )
          }
        ]
      },
      {
        path: '/measure-manage',
        name: 'measure_check_manage',
        meta: {
          icon: 'ios-copy',
          title: '量具质量管理',
          access: ['010405']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'measure-check-wait',
            name: 'measure_check_wait',
            meta: {
              title: '待检列表',
              access: ['01040501']
            },
            component: () =>
              import(
                '@/view/measure-manage/measure-check-manage/wait-check-list.vue'
              )
          },
          {
            path: 'measure-check-list',
            name: 'measure_check_list',
            meta: {
              title: '质检记录',
              access: ['01040502']
            },
            component: () =>
              import(
                '@/view/measure-manage/measure-check-manage/measure-check-list.vue'
              )
          }
        ]
      }
    ]
  },

  {
    path: '/fixture-manage',
    name: 'fixture_manage',
    meta: {
      icon: 'md-share',
      title: '夹具管理',
      access: ['0105']
    },
    component: Main,
    children: [
      {
        path: 'fixture-purchase-list',
        name: 'fixture_purchase_list',
        meta: {
          title: '夹具申购列表',
          access: ['010501']
        },
        component: () =>
          import(
            '@/view/fixture-manage/fixture-purchase/purchase-apply-list.vue'
          )
      },
      {
        path: 'purchase-receive-list',
        name: 'purchase_receive_list',
        meta: {
          title: '夹具申购单接收列表',
          access: ['010502']
        },
        component: () =>
          import(
            '@/view/fixture-manage/fixture-purchase/purchase-receive-list.vue'
          )
      },

      {
        path: 'selfcreate-receive-list',
        name: 'selfcreate_receive_list',
        meta: {
          title: '夹具(自制)接收',
          access: ['010503']
        },
        component: () =>
          import(
            '@/view/fixture-manage/fixture-purchase/selfcreate-receive-list.vue'
          )
      },
      {
        path: 'selfcreate-complete-list',
        name: 'selfcreate_complete_list',
        meta: {
          title: '夹具(自制)完工汇报',
          access: ['010504']
        },
        component: () =>
          import(
            '@/view/fixture-manage/fixture-purchase/selfcreate-complete-list.vue'
          )
      },
      {
        path: '/fixture-check-manage',
        name: 'fixture_check_manage',
        meta: {
          icon: 'ios-copy',
          title: '夹具质量管理',
          access: ['010505']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'fixture-wait-check',
            name: 'fixture_wait_check',
            meta: {
              // icon: "md-list-box",
              title: '待检列表',
              access: ['01050501']
            },
            component: () =>
              import('@/view/fixture-manage/fixture-check/wait-check-list.vue')
          },
          {
            path: 'fixture-check-list',
            name: 'fixture_check_list',
            meta: {
              // icon: "md-list-box",
              title: '夹具检验',
              access: ['01050502']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-check/fixture-check-list.vue'
              )
          },
          {
            path: 'wait-return-list',
            name: 'wait_return_list',
            meta: {
              // icon: "md-list-box",
              title: '待退货',
              access: ['01050503']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-warehouse/wait-return-list.vue'
              )
          }
        ]
      },
      {
        path: '/fixture-warehouse-manage',
        name: 'fixture_warehouse_manage',
        meta: {
          icon: 'md-medal',
          title: '夹具出入库管理',
          access: ['010506']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'purchase-delivery-list',
            name: 'purchase_delivery_list',
            meta: {
              title: '夹具(采购)收货',
              access: ['01050601']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-purchase/purchase-delivery-list.vue'
              )
          },
          {
            path: 'wait-warehouse-list',
            name: 'wait_warehouse_list',
            meta: {
              title: '待入库',
              access: ['01050602']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-warehouse/wait-warehouse-list.vue'
              )
          },
          {
            path: 'fixture-scrap-list',
            name: 'fixture_scrap_list',
            meta: {
              title: '夹具报废列表',
              access: ['01050604']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-scrip-manage/scrip-tabs.vue'
              )
          }
        ]
      },

      {
        path: '/fixture-site-manage',
        name: 'fixture_site_manage',
        meta: {
          icon: 'md-star',
          title: '夹具现场管理',
          access: ['010507']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'fixture-list',
            name: 'fixture_list',
            meta: {
              title: '在库夹具',
              access: ['01050701']
            },
            component: () =>
              import('@/view/fixture-manage/fixture-site/fixture-list.vue')
          },
          {
            path: 'fixture-use-list',
            name: 'fixture_use_list',
            meta: {
              title: '在用夹具',
              access: ['01050702']
            },
            component: () =>
              import('@/view/fixture-manage/fixture-site/fixture-use-list.vue')
          },
          {
            path: 'fixture-maintain-list',
            name: 'fixture_maintain_list',
            meta: {
              // icon: "md-list-box",
              title: '夹具保养列表',
              access: ['01050703']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-site/fixture-maintain-list.vue'
              )
          },
          {
            path: 'spot-check-list',
            name: 'spot_check_list',
            meta: {
              // icon: "md-list-box",
              title: '夹具点检列表',
              access: ['01050704']
            },
            component: () =>
              import('@/view/fixture-manage/fixture-site/spot-check-list.vue')
          }
        ]
      },
      {
        path: '/fixture-repair-manage',
        name: 'fixture_repair_manage',
        meta: {
          icon: 'ios-construct',
          title: '夹具修磨管理',
          access: ['010508']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'wait-repair-list',
            name: 'wait_repair_list',
            meta: {
              // icon: "md-list-box",
              title: '待修磨夹具',
              access: ['01050801']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-repair/wait-repair-list.vue'
              )
          },
          {
            path: 'fixture-repair-list',
            name: 'fixture_repair_list',
            meta: {
              // icon: "md-list-box",
              title: '修磨列表',
              access: ['01050802']
            },
            component: () =>
              import(
                '@/view/fixture-manage/fixture-repair/fixture-repair-list.vue'
              )
          }
          /* {
        path: "set-check-list",
        name: "set_check_list",
        meta: {
          // icon: "md-list-box",
          title: "待送检列表",
          access: ["01050803"]
        },
        component: () =>
          import(
            "@/view/fixture-manage/fixture-repair/set-check-list.vue"
          )
      }, */
        ]
      }
    ]
  },

  {
    path: '/mould-manage',
    name: 'mould_manage',
    meta: {
      icon: 'ios-map',
      title: '模具管理',
      access: ['0106']
    },
    component: Main,
    children: [
      {
        path: '/mould-stockmanage',
        name: 'mould_stock_manage',
        meta: {
          icon: 'md-medal',
          title: '模具库管理',
          access: ['010601']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'mould-wait-warehouse-list',
            name: 'mould_wait_warehouse_list',
            meta: {
              // icon: "md-list-box",
              title: '待入库模具',
              access: ['01060101']
            },
            component: () =>
              import(
                '@/view/mould-manage/mould-list-wait/mould-warehouse-wait.vue'
              )
          },
          {
            path: 'mould-list-manage',
            name: 'mould_list_manage',
            meta: {
              // icon: "md-list-box",
              title: '出入库管理',
              access: ['01060102']
            },
            component: () =>
              import('@/view/mould-manage/mould-list-manage/mould-lists.vue')
          },
          {
            path: 'mould-process-manage',
            name: 'mould_process_page',
            meta: {
              // icon: "md-calculator",
              title: '模具交回',
              access: ['01060103']
            },
            component: () =>
              import(
                '@/view/mould-manage/mould-process-manage/mould-process-manage.vue'
              )
          },
          {
            path: 'mould-scrip-list',
            name: 'mould_scrip_list',
            meta: {
              // icon: "md-trash",
              title: '模具报废',
              access: ['01060104']
            },
            component: () =>
              import('@/view/mould-manage/mould-scrap-manage/scrap-tabs.vue')
          },
          {
            path: 'mould-life-manage',
            name: 'mould_life_manage',
            meta: {
              // icon: "md-list-box",
              title: '生命周期',
              access: ['01060105']
            },
            component: () =>
              import(
                '@/view/mould-manage/mould-life-manage/mould-life-manage.vue'
              )
          }
        ]
      },
      {
        path: '/mould-check-manage',
        name: 'mould_check_manage',
        meta: {
          icon: 'ios-cube',
          title: '量具质量管理',
          access: ['010602']
        },
        component: () =>
          import('@/view/article-publish-center/article-publish-center.vue'),
        children: [
          {
            path: 'mould-check-wait',
            name: 'mould_check_wait',
            meta: {
              // icon: "md-list-box",
              title: '待检列表',
              access: ['01060201']
            },
            component: () =>
              import('@/view/mould-manage/mould-check-manage/check-tabs.vue')
          },
          {
            path: 'mould-lists-unquality',
            name: 'mould_lists_unquality',
            meta: {
              // icon: "md-list-box",
              title: '不合格列表',
              access: ['01060202']
            },
            component: () =>
              import(
                '@/view/mould-manage/mould-list-unquality/mould-list-unquality.vue'
              )
          },
          {
            path: 'mould-repair-manage',
            name: 'mould_repair_manage',
            meta: {
              // icon: "md-build",
              title: '模具修磨',
              access: ['01060203']
            },
            component: () =>
              import('@/view/mould-manage/mould-repair-manage/repair-tabs.vue')
          }
        ]
      }
    ]
  },
  {
    path: '/argu',
    name: 'argu',
    meta: {
      hideInMenu: true
    },
    component: Main,
    children: [
      {
        path: 'params/:id',
        name: 'params',
        meta: {
          icon: 'md-flower',
          title: route => `{{ params }}-${route.params.id}`,
          notCache: true,
          beforeCloseName: 'before_close_normal'
        },
        component: () => import('@/view/argu-page/params.vue')
      },
      {
        path: 'query',
        name: 'query',
        meta: {
          icon: 'md-flower',
          title: route => `{{ query }}-${route.query.id}`,
          notCache: true
        },
        component: () => import('@/view/argu-page/query.vue')
      }
    ]
  },
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/500.vue')
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/404.vue')
  }
]
