package userservice;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class UserProfileServiceGrpc {

  private UserProfileServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "UserProfileService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<userservice.CreateUserProfileRequest,
      userservice.CreateUserProfileResponse> getCreateUserProfileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateUserProfile",
      requestType = userservice.CreateUserProfileRequest.class,
      responseType = userservice.CreateUserProfileResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<userservice.CreateUserProfileRequest,
      userservice.CreateUserProfileResponse> getCreateUserProfileMethod() {
    io.grpc.MethodDescriptor<userservice.CreateUserProfileRequest, userservice.CreateUserProfileResponse> getCreateUserProfileMethod;
    if ((getCreateUserProfileMethod = UserProfileServiceGrpc.getCreateUserProfileMethod) == null) {
      synchronized (UserProfileServiceGrpc.class) {
        if ((getCreateUserProfileMethod = UserProfileServiceGrpc.getCreateUserProfileMethod) == null) {
          UserProfileServiceGrpc.getCreateUserProfileMethod = getCreateUserProfileMethod =
              io.grpc.MethodDescriptor.<userservice.CreateUserProfileRequest, userservice.CreateUserProfileResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateUserProfile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  userservice.CreateUserProfileRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  userservice.CreateUserProfileResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserProfileServiceMethodDescriptorSupplier("CreateUserProfile"))
              .build();
        }
      }
    }
    return getCreateUserProfileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<userservice.GetUserProfileRequest,
      userservice.UserProfile> getGetUserProfileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUserProfile",
      requestType = userservice.GetUserProfileRequest.class,
      responseType = userservice.UserProfile.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<userservice.GetUserProfileRequest,
      userservice.UserProfile> getGetUserProfileMethod() {
    io.grpc.MethodDescriptor<userservice.GetUserProfileRequest, userservice.UserProfile> getGetUserProfileMethod;
    if ((getGetUserProfileMethod = UserProfileServiceGrpc.getGetUserProfileMethod) == null) {
      synchronized (UserProfileServiceGrpc.class) {
        if ((getGetUserProfileMethod = UserProfileServiceGrpc.getGetUserProfileMethod) == null) {
          UserProfileServiceGrpc.getGetUserProfileMethod = getGetUserProfileMethod =
              io.grpc.MethodDescriptor.<userservice.GetUserProfileRequest, userservice.UserProfile>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUserProfile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  userservice.GetUserProfileRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  userservice.UserProfile.getDefaultInstance()))
              .setSchemaDescriptor(new UserProfileServiceMethodDescriptorSupplier("GetUserProfile"))
              .build();
        }
      }
    }
    return getGetUserProfileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<userservice.DeleteUserProfileRequest,
      userservice.DeleteUserProfileResponse> getDeleteUserProfileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteUserProfile",
      requestType = userservice.DeleteUserProfileRequest.class,
      responseType = userservice.DeleteUserProfileResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<userservice.DeleteUserProfileRequest,
      userservice.DeleteUserProfileResponse> getDeleteUserProfileMethod() {
    io.grpc.MethodDescriptor<userservice.DeleteUserProfileRequest, userservice.DeleteUserProfileResponse> getDeleteUserProfileMethod;
    if ((getDeleteUserProfileMethod = UserProfileServiceGrpc.getDeleteUserProfileMethod) == null) {
      synchronized (UserProfileServiceGrpc.class) {
        if ((getDeleteUserProfileMethod = UserProfileServiceGrpc.getDeleteUserProfileMethod) == null) {
          UserProfileServiceGrpc.getDeleteUserProfileMethod = getDeleteUserProfileMethod =
              io.grpc.MethodDescriptor.<userservice.DeleteUserProfileRequest, userservice.DeleteUserProfileResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteUserProfile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  userservice.DeleteUserProfileRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  userservice.DeleteUserProfileResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserProfileServiceMethodDescriptorSupplier("DeleteUserProfile"))
              .build();
        }
      }
    }
    return getDeleteUserProfileMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserProfileServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserProfileServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserProfileServiceStub>() {
        @java.lang.Override
        public UserProfileServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserProfileServiceStub(channel, callOptions);
        }
      };
    return UserProfileServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static UserProfileServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserProfileServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserProfileServiceBlockingV2Stub>() {
        @java.lang.Override
        public UserProfileServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserProfileServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return UserProfileServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserProfileServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserProfileServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserProfileServiceBlockingStub>() {
        @java.lang.Override
        public UserProfileServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserProfileServiceBlockingStub(channel, callOptions);
        }
      };
    return UserProfileServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserProfileServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserProfileServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserProfileServiceFutureStub>() {
        @java.lang.Override
        public UserProfileServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserProfileServiceFutureStub(channel, callOptions);
        }
      };
    return UserProfileServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createUserProfile(userservice.CreateUserProfileRequest request,
        io.grpc.stub.StreamObserver<userservice.CreateUserProfileResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateUserProfileMethod(), responseObserver);
    }

    /**
     */
    default void getUserProfile(userservice.GetUserProfileRequest request,
        io.grpc.stub.StreamObserver<userservice.UserProfile> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserProfileMethod(), responseObserver);
    }

    /**
     */
    default void deleteUserProfile(userservice.DeleteUserProfileRequest request,
        io.grpc.stub.StreamObserver<userservice.DeleteUserProfileResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteUserProfileMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UserProfileService.
   */
  public static abstract class UserProfileServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UserProfileServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UserProfileService.
   */
  public static final class UserProfileServiceStub
      extends io.grpc.stub.AbstractAsyncStub<UserProfileServiceStub> {
    private UserProfileServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserProfileServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserProfileServiceStub(channel, callOptions);
    }

    /**
     */
    public void createUserProfile(userservice.CreateUserProfileRequest request,
        io.grpc.stub.StreamObserver<userservice.CreateUserProfileResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateUserProfileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUserProfile(userservice.GetUserProfileRequest request,
        io.grpc.stub.StreamObserver<userservice.UserProfile> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserProfileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteUserProfile(userservice.DeleteUserProfileRequest request,
        io.grpc.stub.StreamObserver<userservice.DeleteUserProfileResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteUserProfileMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UserProfileService.
   */
  public static final class UserProfileServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<UserProfileServiceBlockingV2Stub> {
    private UserProfileServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserProfileServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserProfileServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public userservice.CreateUserProfileResponse createUserProfile(userservice.CreateUserProfileRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateUserProfileMethod(), getCallOptions(), request);
    }

    /**
     */
    public userservice.UserProfile getUserProfile(userservice.GetUserProfileRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetUserProfileMethod(), getCallOptions(), request);
    }

    /**
     */
    public userservice.DeleteUserProfileResponse deleteUserProfile(userservice.DeleteUserProfileRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDeleteUserProfileMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service UserProfileService.
   */
  public static final class UserProfileServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UserProfileServiceBlockingStub> {
    private UserProfileServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserProfileServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserProfileServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public userservice.CreateUserProfileResponse createUserProfile(userservice.CreateUserProfileRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateUserProfileMethod(), getCallOptions(), request);
    }

    /**
     */
    public userservice.UserProfile getUserProfile(userservice.GetUserProfileRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserProfileMethod(), getCallOptions(), request);
    }

    /**
     */
    public userservice.DeleteUserProfileResponse deleteUserProfile(userservice.DeleteUserProfileRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteUserProfileMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UserProfileService.
   */
  public static final class UserProfileServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<UserProfileServiceFutureStub> {
    private UserProfileServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserProfileServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserProfileServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<userservice.CreateUserProfileResponse> createUserProfile(
        userservice.CreateUserProfileRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateUserProfileMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<userservice.UserProfile> getUserProfile(
        userservice.GetUserProfileRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserProfileMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<userservice.DeleteUserProfileResponse> deleteUserProfile(
        userservice.DeleteUserProfileRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteUserProfileMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_USER_PROFILE = 0;
  private static final int METHODID_GET_USER_PROFILE = 1;
  private static final int METHODID_DELETE_USER_PROFILE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_USER_PROFILE:
          serviceImpl.createUserProfile((userservice.CreateUserProfileRequest) request,
              (io.grpc.stub.StreamObserver<userservice.CreateUserProfileResponse>) responseObserver);
          break;
        case METHODID_GET_USER_PROFILE:
          serviceImpl.getUserProfile((userservice.GetUserProfileRequest) request,
              (io.grpc.stub.StreamObserver<userservice.UserProfile>) responseObserver);
          break;
        case METHODID_DELETE_USER_PROFILE:
          serviceImpl.deleteUserProfile((userservice.DeleteUserProfileRequest) request,
              (io.grpc.stub.StreamObserver<userservice.DeleteUserProfileResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCreateUserProfileMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              userservice.CreateUserProfileRequest,
              userservice.CreateUserProfileResponse>(
                service, METHODID_CREATE_USER_PROFILE)))
        .addMethod(
          getGetUserProfileMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              userservice.GetUserProfileRequest,
              userservice.UserProfile>(
                service, METHODID_GET_USER_PROFILE)))
        .addMethod(
          getDeleteUserProfileMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              userservice.DeleteUserProfileRequest,
              userservice.DeleteUserProfileResponse>(
                service, METHODID_DELETE_USER_PROFILE)))
        .build();
  }

  private static abstract class UserProfileServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserProfileServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return userservice.UserService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserProfileService");
    }
  }

  private static final class UserProfileServiceFileDescriptorSupplier
      extends UserProfileServiceBaseDescriptorSupplier {
    UserProfileServiceFileDescriptorSupplier() {}
  }

  private static final class UserProfileServiceMethodDescriptorSupplier
      extends UserProfileServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UserProfileServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UserProfileServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserProfileServiceFileDescriptorSupplier())
              .addMethod(getCreateUserProfileMethod())
              .addMethod(getGetUserProfileMethod())
              .addMethod(getDeleteUserProfileMethod())
              .build();
        }
      }
    }
    return result;
  }
}
