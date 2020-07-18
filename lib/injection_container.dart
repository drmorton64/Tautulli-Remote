import 'package:connectivity/connectivity.dart';
import 'package:device_info/device_info.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import 'core/device_info/device_info.dart';
import 'core/helpers/connection_address_helper.dart';
import 'core/helpers/tautulli_api_url_helper.dart';
import 'core/network/network_info.dart';
import 'features/activity/data/datasources/activity_data_source.dart';
import 'features/activity/data/datasources/geo_ip_data_source.dart';
import 'features/activity/data/repositories/activity_repository_impl.dart';
import 'features/activity/data/repositories/geo_ip_repository_impl.dart';
import 'features/activity/domain/repositories/activity_repository.dart';
import 'features/activity/domain/repositories/geo_ip_repository.dart';
import 'features/activity/domain/usecases/get_activity.dart';
import 'features/activity/domain/usecases/get_geo_ip.dart';
import 'features/activity/presentation/bloc/activity_bloc.dart';
import 'features/onesignal/data/datasources/onesignal_data_source.dart';
import 'features/onesignal/presentation/bloc/onesignal_health_bloc.dart';
import 'features/onesignal/presentation/bloc/onesignal_privacy_bloc.dart';
import 'features/onesignal/presentation/bloc/onesignal_subscription_bloc.dart';
import 'features/settings/data/datasources/register_device_data_source.dart';
import 'features/settings/data/datasources/settings_data_source.dart';
import 'features/settings/data/repositories/register_device_repository_impl.dart';
import 'features/settings/data/repositories/settings_repository_impl.dart';
import 'features/settings/domain/repositories/register_device_repository.dart';
import 'features/settings/domain/repositories/settings_repository.dart';
import 'features/settings/domain/usecases/get_settings.dart';
import 'features/settings/domain/usecases/register_device.dart';
import 'features/settings/domain/usecases/set_settings.dart';
import 'features/settings/domain/usecases/update_device_registration.dart';
import 'features/settings/presentation/bloc/register_device_bloc.dart';
import 'features/settings/presentation/bloc/settings_bloc.dart';

// Service locator alias
final sl = GetIt.instance;

Future<void> init() async {
  //! Features - Settings
  // Bloc
  sl.registerFactory(
    () => SettingsBloc(
      getSettings: sl(),
      setSettings: sl(),
    ),
  );

  sl.registerFactory(
    () =>
        RegisterDeviceBloc(registerDevice: sl(), connectionAddressHelper: sl()),
  );

  // Use case
  sl.registerLazySingleton(
    () => GetSettings(repository: sl()),
  );

  sl.registerLazySingleton(
    () => SetSettings(repository: sl()),
  );

  sl.registerLazySingleton(
    () => RegisterDevice(repository: sl()),
  );

  sl.registerLazySingleton(
    () => UpdateDeviceRegistration(
      repository: sl(),
      getSettings: sl(),
    ),
  );

  // Repository
  sl.registerLazySingleton<SettingsRepository>(
    () => SettingsRepositoryImpl(dataSource: sl()),
  );

  sl.registerLazySingleton<RegisterDeviceRepository>(
    () => RegisterDeviceRepositoryImpl(
      dataSource: sl(),
      networkInfo: sl(),
    ),
  );

  // Data sources
  sl.registerLazySingleton<SettingsDataSource>(
    () => SettingsDataSourceImpl(
      sharedPreferences: sl(),
      connectionAddressHelper: sl(),
    ),
  );

  sl.registerLazySingleton<RegisterDeviceDataSource>(
    () => RegisterDeviceDataSourceImpl(
      client: sl(),
      setSettings: sl(),
      tautulliApiUrls: sl(),
      deviceInfo: sl(),
      oneSignal: sl(),
    ),
  );

  //! Features - OneSignal
  // Bloc
  sl.registerFactory(
    () => OneSignalHealthBloc(
      oneSignal: sl(),
    ),
  );

  sl.registerFactory(
    () => OneSignalPrivacyBloc(
      oneSignal: sl(),
      getSettings: sl(),
      registerDevice: sl(),
      // updateDeviceRegistration: sl()
    ),
  );

  sl.registerFactory(
    () => OneSignalSubscriptionBloc(
      oneSignal: sl(),
    ),
  );

  // Data sources
  sl.registerLazySingleton<OneSignalDataSource>(
    () => OneSignalDataSourceImpl(
      client: sl(),
      networkInfo: sl(),
    ),
  );

  //! Features - Activity
  // Bloc
  sl.registerFactory(
    () => ActivityBloc(
      activity: sl(),
      geoIp: sl(),
      tautulliApiUrls: sl(),
    ),
  );

  // Use case
  sl.registerLazySingleton(
    () => GetActivity(
      repository: sl(),
    ),
  );

  sl.registerLazySingleton(
    () => GetGeoIp(
      repository: sl(),
    ),
  );

  // Repository
  sl.registerLazySingleton<ActivityRepository>(
    () => ActivityRepositoryImpl(
      dataSource: sl(),
      networkInfo: sl(),
    ),
  );

  sl.registerLazySingleton<GeoIpRepository>(
    () => GeoIpRepositoryImpl(
      dataSource: sl(),
      networkInfo: sl(),
    ),
  );

  // Data sources
  sl.registerLazySingleton<ActivityDataSource>(
    () => ActivityDataSourceImpl(
      client: sl(),
      getSettings: sl(),
      tautulliApiUrls: sl(),
    ),
  );

  sl.registerLazySingleton<GeoIpDataSource>(
    () => GeoIpDataSourceImpl(
      client: sl(),
      getSettings: sl(),
      tautulliApiUrls: sl(),
    ),
  );

  //! Core
  sl.registerLazySingleton<NetworkInfo>(
    () => NetworkInfoImpl(
      sl(),
    ),
  );

  sl.registerLazySingleton<TautulliApiUrls>(
    () => TautulliApiUrlsImpl(
      getSettings: sl(),
    ),
  );

  sl.registerLazySingleton<ConnectionAddressHelper>(
    () => ConnectionAddressHelperImpl(),
  );

  sl.registerLazySingleton<DeviceInfo>(
    () => DeviceInfoImpl(
      sl(),
    ),
  );

  //! External
  final sharedPreferences = await SharedPreferences.getInstance();
  sl.registerLazySingleton(() => sharedPreferences);
  sl.registerLazySingleton(() => http.Client());
  sl.registerLazySingleton(() => Connectivity());
  sl.registerLazySingleton(() => DeviceInfoPlugin());
}