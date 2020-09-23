import 'package:flutter/material.dart';

import '../../../../core/helpers/color_palette_helper.dart';
import '../../../../core/helpers/time_format_helper.dart';
import '../../domain/entities/statistics.dart';

class StatisticsDetails extends StatelessWidget {
  final Statistics statistic;

  const StatisticsDetails({Key key, @required this.statistic})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        // _rowOne(statistic),
        Text(
          _rowOne(),
          overflow: TextOverflow.ellipsis,
          maxLines: 2,
          style: TextStyle(
            fontSize: 18,
            color: TautulliColorPalette.not_white,
          ),
        ),
        Text(
          _rowTwo(),
          overflow: TextOverflow.ellipsis,
          style: TextStyle(
            fontSize: 15,
            color: TautulliColorPalette.not_white,
          ),
        ),
        Text(
          _rowThree(),
          overflow: TextOverflow.ellipsis,
          style: TextStyle(
            fontSize: 15,
            color: TautulliColorPalette.not_white,
          ),
        ),
      ],
    );
  }

  String _rowOne() {
    switch (statistic.statId) {
      case ('top_tv'):
      case ('popular_tv'):
      case ('top_movies'):
      case ('popular_movies'):
      case ('top_music'):
      case ('popular_music'):
      case ('last_watched'):
      case ('most_concurrent'):
        return statistic.title;
      case ('top_platforms'):
        return statistic.platform;
      case ('top_users'):
        return statistic.friendlyName;
      default:
        return 'UNKNOWN';
    }
  }

  String _rowTwo() {
    switch (statistic.statId) {
      case ('top_tv'):
      case ('top_movies'):
      case ('top_music'):
      case ('top_platforms'):
      case ('top_users'):
        return '${statistic.totalPlays.toString()} plays';
      case ('popular_tv'):
      case ('popular_movies'):
      case ('popular_music'):
        return '${statistic.usersWatched} users';
      case ('last_watched'):
        return statistic.friendlyName;
      case ('most_concurrent'):
        return '${statistic.count.toString()} streams';
      default:
        return statistic.statId;
    }
  }

  String _rowThree() {
    switch (statistic.statId) {
      case ('top_tv'):
      case ('top_movies'):
      case ('top_music'):
      case ('top_platforms'):
      case ('top_users'):
        return TimeFormatHelper.pretty(statistic.totalDuration);
      case ('popular_tv'):
      case ('popular_movies'):
      case ('popular_music'):
      case ('last_watched'):
        return TimeFormatHelper.timeAgo(statistic.lastWatch);
      case ('most_concurrent'):
        return TimeFormatHelper.cleanDateTime(statistic.started);
      default:
        return statistic.statId;
    }
  }
}